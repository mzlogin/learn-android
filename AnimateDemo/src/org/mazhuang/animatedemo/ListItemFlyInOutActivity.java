package org.mazhuang.animatedemo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ListItemFlyInOutActivity extends Activity {
    
    private ExpandableListView mList;
    private Button mClearList;
    private MyExpandableAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_fly_in_out);
        
        mListAdapter = new MyExpandableAdapter();
        mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (mListAdapter.getGroupCount() != 0) {
                    deleteItems();
                }
            }
        });
        mList = (ExpandableListView)findViewById(R.id.expandable_list);
        mList.setAdapter(mListAdapter);
        
        Animation animation = (Animation)AnimationUtils.loadAnimation(this, R.anim.fly_right_in);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.8f);
        mList.setLayoutAnimation(controller);
        for (int i = 0; i < mListAdapter.getGroupCount(); i++) {
            mList.expandGroup(i);
        }
        
        mClearList = (Button)findViewById(R.id.btn_clear_list);
        mClearList.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                deleteItems();
            }
        });
    }
    
    public void deleteItems() {
        View item = mList.getChildAt(mListAdapter.getFirstDeletableItemPos());
        
        if (item == null) {
            return;
        }
        
        final Animation animation = (Animation) AnimationUtils.loadAnimation(ListItemFlyInOutActivity.this, R.anim.fly_left_out);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {}
 
            public void onAnimationRepeat(Animation animation) {}
 
            @TargetApi(15)
            public void onAnimationEnd(Animation animation) {
                mListAdapter.deleteItem();
            }
        });
 
        item.startAnimation(animation);
    }
    
    class MyExpandableAdapter extends BaseExpandableListAdapter {

        private List<List<String>> mData;
        
        public MyExpandableAdapter() {
            mData = new ArrayList<List<String>>();
            
            List<String> group1 = new ArrayList<String>();
            for (int i = 0; i < 3; i ++) {
                group1.add("hello item" + i);
            }
            mData.add(group1);
            
            List<String> group2 = new ArrayList<String>();
            for (int i = 0; i < 4; i ++) {
                group2.add("world item" + i);
            }
            mData.add(group2);
            
            List<String> group3 = new ArrayList<String>();
            for (int i = 0; i < 5; i ++) {
                group3.add("ha item" + i);
            }
            
            mData.add(group3);
        }
        
        @Override
        public int getGroupCount() {
            if (mData == null) {
                return 0;
            } else {
                return mData.size();
            }
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int count = 0;
            
            if (mData != null && mData.size() > groupPosition) {
                if (mData.get(groupPosition) != null) {
                    count = mData.get(groupPosition).size();
                }
            }
            
            return count;
        }

        @Override
        public Object getGroup(int groupPosition) {
            if (mData != null && mData.size() > groupPosition) {
                return mData.get(groupPosition);
            } 
            
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (mData != null && mData.size() > groupPosition) {
                if (mData.get(groupPosition).size() > childPosition) {
                    return mData.get(groupPosition).get(childPosition);
                }
            }
            
            return null;                
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {            
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            TextView view = null;
            if (convertView != null) {
                view = (TextView)convertView;
               
            } else {
                view = new TextView(ListItemFlyInOutActivity.this);
            } 
            view.setBackgroundColor(Color.parseColor("#0000FF"));
            view.setText("" + groupPosition);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            TextView view = null;
            if (convertView != null) {
                view = (TextView)convertView;
            } else {
                view = new TextView(ListItemFlyInOutActivity.this);
            }
            view.setBackgroundColor(Color.parseColor("#00FF00"));
            view.setText("" + mData.get(groupPosition).get(childPosition));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        
        public int getFirstDeletableItemPos() {
            int pos = 0;
            
            if (mData != null && mData.size() > 0) {
                if (mData.get(0) != null && mData.get(0).size() > 0) {
                    pos = 1;
                }
            }
            
            return pos;
        }
        
        public int deleteItem() {
            int pos = -1;
            if (mData != null && mData.size() > 0) {
                if (mData.get(0) != null) {
                    if (mData.get(0).size() > 0) {
                        mData.get(0).remove(0);
                        pos = 1;
                    } else {
                        mData.remove(0);
                        pos = 0;
                    }
                    notifyDataSetChanged();
                } else {
                    mData.remove(0);
                    pos = 0;
                    notifyDataSetChanged();
                }
            }
            
            return pos;
        }
    }
}
