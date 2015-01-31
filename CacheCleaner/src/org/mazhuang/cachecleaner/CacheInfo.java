package org.mazhuang.cachecleaner;

/**
 * 缓存项
 * 
 * @author mazhuang
 *
 */
public class CacheInfo {
	private String mPackageName;
	private String mPath;
	private boolean mRecommandClean;
	public String getPackageName() {
		return mPackageName;
	}
	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}
	public String getPath() {
		return mPath;
	}
	public void setPath(String path) {
		mPath = path;
	}
	public boolean isRecommandClean() {
		return mRecommandClean;
	}
	public void setRecommandClean(boolean recommandClean) {
		mRecommandClean = recommandClean;
	}
}
