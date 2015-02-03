package org.mazhuang.cachecleaner;

public interface ICacheCleanCallBack {
	public void onCleanStart();
	public void onCleanFinish();
	public void onCleanProgress(int pos);
}
