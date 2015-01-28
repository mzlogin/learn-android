package org.mazhuang.cachecleaner;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
		Boolean isRoot = ShellUtils.checkRootPermission();
		mCallBack.onScanProgress(50);
		return isRoot;
	}
	
	@Override
	protected void onPostExecute(Boolean isRoot) {
		mCallBack.onScanFinish();
		Toast toast = Toast.makeText(mAppContext, isRoot ? "root" : "no root", Toast.LENGTH_LONG);
		toast.show();
	}
}
