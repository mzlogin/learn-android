package org.mazhuang.cachecleaner;

public interface ICacheScanCallBack {
	public void onScanStart();
	public void onScanFinish();
	public void onScanProgress(int pos);
}
