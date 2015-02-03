package org.mazhuang.cachecleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mazhuang.cachecleaner.ShellUtils.CommandResult;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;

public class CacheCleanTask extends AsyncTask<Void, Void, Boolean> {
	
	private ICacheCleanCallBack mCallBack;
	private Context mAppContext;
	
	public CacheCleanTask(Context context, ICacheCleanCallBack callBack) {
		mAppContext = context;
		mCallBack = callBack;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mCallBack.onCleanStart();
		
		ArrayList<String> commands = new ArrayList<String>();
		
		ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
		
		boolean isAboveFroyo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
		for (int i = 0; i < caches.size(); i++) {
			CacheInfo cacheInfo = caches.get(i);
			if (cacheInfo.isChecked()) {
				try {
					Context context = mAppContext.createPackageContext(cacheInfo.getPackageName(), 
							Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
					
					File cacheDir = context.getCacheDir();
					String command;
					if (cacheDir != null) {
						command = "rm -r " + cacheDir;
						commands.add(command);
					}
					
					if (isAboveFroyo) {
						File externalCacheDir = context.getExternalCacheDir();
						if (externalCacheDir != null) {
							command = "rm -r " + externalCacheDir;
							commands.add(command);
						}
					}
				
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		ShellUtils.execCommand(commands, true);
		caches.clear();
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean isRoot) {
		mCallBack.onCleanFinish();
	}
}
