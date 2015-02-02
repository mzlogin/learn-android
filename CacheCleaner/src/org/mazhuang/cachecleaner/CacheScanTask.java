package org.mazhuang.cachecleaner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mazhuang.cachecleaner.ShellUtils.CommandResult;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.RemoteException;

public class CacheScanTask extends AsyncTask<Void, Void, Boolean> {
	
	private ICacheScanCallBack mCallBack;
	private Context mAppContext;
	
	public CacheScanTask(Context context, ICacheScanCallBack callBack) {
		mAppContext = context;
		mCallBack = callBack;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mCallBack.onScanStart();
		
		ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
		caches.clear();
		
		PackageManager pm = mAppContext.getPackageManager();
		List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
		
		IPackageStatsObserver.Stub observer = new PackageSizeObserver();
		for (int i = 0; i < installedPackages.size(); i++) {
			ApplicationInfo appInfo = installedPackages.get(i);
			getPackageInfo(appInfo.packageName, observer);
			mCallBack.onScanProgress(100 * i / installedPackages.size());
		}
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean isRoot) {
		mCallBack.onScanFinish();
	}
	
	public void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {
		try {
			PackageManager pm = mAppContext.getPackageManager();
			Method getPackageSizeInfo = pm.getClass()
					.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
			
			getPackageSizeInfo.invoke(pm, packageName, observer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class PackageSizeObserver extends IPackageStatsObserver.Stub {

		@Override
		public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded)
				throws RemoteException {
			if (packageStats == null || !succeeded) {
				return;
			}
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setPackageName(packageStats.packageName);
			cacheInfo.setCacheSize(packageStats.cacheSize);
			
			ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
			caches.add(cacheInfo);
		}
		
	}
}
