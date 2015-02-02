package org.mazhuang.cachecleaner;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CacheListFragment extends ListFragment {
	private ArrayList<CacheInfo> mCaches;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCaches = CacheLab.get(getActivity()).getCaches();
		
		CacheAdapter adapter = new CacheAdapter(mCaches);
		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	
	public void updateUI() {
		((CacheAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private class CacheAdapter extends ArrayAdapter<CacheInfo> {
		public CacheAdapter(ArrayList<CacheInfo> caches) {
			super(getActivity(), 0, caches);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_cache, null);
			}
			
			CacheInfo c = getItem(position);
			
			TextView packageNameTextView = (TextView)convertView.findViewById(R.id.cache_list_item_packageName);
			packageNameTextView.setText(c.getPackageName());
			
			TextView pathTextView = (TextView)convertView.findViewById(R.id.cache_list_item_size);
			pathTextView.setText("" + c.getCacheSize());
			
			CheckBox recommentCheckBox = (CheckBox)convertView.findViewById(R.id.cache_list_item_recommandCheckBox);
			recommentCheckBox.setChecked(true);
			
			return convertView;
		}
	}
}
