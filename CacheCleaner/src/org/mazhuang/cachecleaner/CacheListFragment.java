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
	private ArrayList<Cache> mCaches;
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
		((CacheAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private class CacheAdapter extends ArrayAdapter<Cache> {
		public CacheAdapter(ArrayList<Cache> caches) {
			super(getActivity(), 0, caches);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_cache, null);
			}
			
			Cache c = getItem(position);
			
			TextView packageNameTextView = (TextView)convertView.findViewById(R.id.cache_list_item_packageName);
			packageNameTextView.setText(c.getPackageName());
			
			TextView pathTextView = (TextView)convertView.findViewById(R.id.cache_list_item_path);
			pathTextView.setText(c.getPath());
			
			CheckBox recommentCheckBox = (CheckBox)convertView.findViewById(R.id.cache_list_item_recommandCheckBox);
			recommentCheckBox.setChecked(c.isRecommandClean());
			
			return convertView;
		}
	}
}
