package org.mazhuang.cachecleaner;

/**
 * 缓存项
 * 
 * @author mazhuang
 *
 */
public class CacheInfo {
	private String mPackageName;
	private long mCacheSize;
	public String getPackageName() {
		return mPackageName;
	}
	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}
	public long getCacheSize() {
		return mCacheSize;
	}
	public void setCacheSize(long cacheSize) {
		mCacheSize = cacheSize;
	}
}
