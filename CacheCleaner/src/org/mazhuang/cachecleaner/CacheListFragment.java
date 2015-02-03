package org.mazhuang.cachecleaner;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CacheListFragment extends ListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CacheAdapter adapter = new CacheAdapter(CacheLab.get(getActivity()).getCaches());
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
			
			CacheInfo cacheInfo = getItem(position);
			
			TextView packageNameTextView = (TextView)convertView.findViewById(R.id.cache_list_item_packageName);
			packageNameTextView.setText(cacheInfo.getPackageName());
			
			TextView pathTextView = (TextView)convertView.findViewById(R.id.cache_list_item_size);
			pathTextView.setText("" + cacheInfo.getCacheSize());
			
			CheckBox recommendCheckBox = (CheckBox)convertView.findViewById(R.id.cache_list_item_recommandCheckBox);
			recommendCheckBox.setChecked(cacheInfo.isChecked());
			recommendCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					CacheInfo cacheInfo = (CacheInfo)(((View)(buttonView.getParent())).getTag());
					cacheInfo.setChecked(isChecked);
				}
			});
			
			convertView.setTag(cacheInfo);
			
			return convertView;
		}
	}
}
