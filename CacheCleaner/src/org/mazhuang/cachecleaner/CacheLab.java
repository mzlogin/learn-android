package org.mazhuang.cachecleaner;

import java.util.ArrayList;

import android.content.Context;

public class CacheLab {
	private ArrayList<CacheInfo> mCaches;
	private static CacheLab sCacheLab;
	private Context mAppContext;
	
	private CacheLab(Context appContext) {
		mAppContext = appContext;
		mCaches = new ArrayList<CacheInfo>();
	}
	
	public static CacheLab get(Context c) {
		if (sCacheLab == null) {
			synchronized (CacheLab.class) {
				if (sCacheLab == null) {
					sCacheLab = new CacheLab(c.getApplicationContext());
				}
			}
		}
		
		return sCacheLab;
	}
	
	public ArrayList<CacheInfo> getCaches() {
		return mCaches;
	}
}
