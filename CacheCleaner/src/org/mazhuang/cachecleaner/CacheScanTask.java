package org.mazhuang.cachecleaner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

public class CacheScanTask extends AsyncTask<Void, Void, Boolean> {
	
	private ICacheScanCallBack mCallBack;
	private Context mAppContext;
	private int mScanCount = 0;
	private int mTotalCount = 0;
	
	public CacheScanTask(Context context, ICacheScanCallBack callBack) {
		mAppContext = context;
		mCallBack = callBack;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		mCallBack.onScanStart();
		
		ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
		caches.clear();
		mScanCount = 0;
		mTotalCount = 0;
		
		PackageManager pm = mAppContext.getPackageManager();
		List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);
		
		IPackageStatsObserver.Stub observer = new PackageSizeObserver();
		mTotalCount = installedPackages.size();
		for (int i = 0; i < mTotalCount; i++) {
			ApplicationInfo appInfo = installedPackages.get(i);
			// NOTICE!This call is not synchronize
			getPackageInfo(appInfo.packageName, observer);
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
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded)
				throws RemoteException {
			mScanCount++;
			if (packageStats == null || !succeeded) {
			} else {
				mCallBack.onScanProgress((int)(mScanCount * 100 / mTotalCount));
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.setPackageName(packageStats.packageName);
				cacheInfo.setCacheSize(packageStats.cacheSize);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					cacheInfo.setCacheSize(packageStats.cacheSize + packageStats.externalCacheSize);
				}
				cacheInfo.setChecked(true);
				
				ArrayList<CacheInfo> caches = CacheLab.get(mAppContext).getCaches();
				if (cacheInfo.getCacheSize() != 0) {
					caches.add(cacheInfo);
				}
			}
			if (mScanCount == mTotalCount) {
				mCallBack.onScanFinish();
			}
		}
	}
}
