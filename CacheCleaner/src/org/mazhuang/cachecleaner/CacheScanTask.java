package org.mazhuang.cachecleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mazhuang.cachecleaner.ShellUtils.CommandResult;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

public class CacheScanTask extends AsyncTask<Void, Void, Boolean> {
	
	private ICacheScanCallBack mCallBack;
	private Context mAppContext;
	
	private static final String GET_DIR_SIZE_COMMAND = "du -d 2 ";

	public CacheScanTask(Context context, ICacheScanCallBack callBack) {
		mAppContext = context;
		mCallBack = callBack;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mCallBack.onScanStart();
		
		ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
		caches.clear();
		
		// step1: get all installed packages
		PackageManager pm = mAppContext.getPackageManager();
		List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
		
		// step2: scan
		boolean isAboveFroyo = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
		ArrayList<String> commands = new ArrayList<String>();
		for (ApplicationInfo appInfo : installedPackages) {
			try {
				Context context = mAppContext.createPackageContext(appInfo.packageName, 
						Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
				File filesDir = context.getFilesDir();
				File cacheDir = context.getCacheDir();
				
				String command = GET_DIR_SIZE_COMMAND + filesDir;
				commands.add(command);
				
				command = GET_DIR_SIZE_COMMAND + cacheDir;
				commands.add(command);
				
				if (isAboveFroyo) {
					// crash at Nexus 4  v4.4.4
//					File externalCacheDir = context.getExternalCacheDir();
//					command = GET_DIR_SIZE_COMMAND + externalCacheDir;
//					commands.add(command);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		CommandResult result = ShellUtils.execCommand(commands, true, true);
		
		// step3: parser output
		String[] sucStrs = result.successMsg.split("\t");
		
		for (String str : sucStrs) {
			CacheInfo cache = new CacheInfo();
			cache.setPackageName(str);
			caches.add(cache);
		}
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean isRoot) {
		mCallBack.onScanFinish();
	}
}
