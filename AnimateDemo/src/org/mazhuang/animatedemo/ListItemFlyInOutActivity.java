package org.mazhuang.animatedemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ListItemFlyInOutActivity extends Activity {
    
    private static final String TAG = "ListItemFlyInOutActivity";
    
    private ExpandableListView mList;
    private Button mClearList;
    private MyExpandableAdapter mListAdapter;
    
    private static final int MSG_HANDLER_DELETE_ITEM = 0x01;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HANDLER_DELETE_ITEM:
                    deleteItems();
                    break;
                    
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_fly_in_out);
        
        mListAdapter = new MyExpandableAdapter();
        mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (mListAdapter.getGroupCount() != 0) {
                    //deleteItems();
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
                //deleteItems();
                mHandler.sendEmptyMessage(MSG_HANDLER_DELETE_ITEM);
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
            public void onAnimationStart(Animation animation) {
            }
 
            public void onAnimationRepeat(Animation animation) {
            }
 
            public void onAnimationEnd(Animation animation) {
                mListAdapter.deleteItem();
                //deleteItems();
                mHandler.sendEmptyMessage(MSG_HANDLER_DELETE_ITEM);
            }
        });
 
        Log.d(TAG, "startAnimation " + ((TextView)item).getText());
        item.startAnimation(animation);
    }
    
    class MyExpandableAdapter extends BaseExpandableListAdapter {

        private List<String> mGroupData;
        private List<List<String>> mChildrenData;
        
        public MyExpandableAdapter() {
            mGroupData = new ArrayList<String>();
            mGroupData.add("平台");
            mGroupData.add("语言");
            mGroupData.add("设计模式");
            
            mChildrenData = new ArrayList<List<String>>();
            List<String> childrenPlatform = new ArrayList<String>();
            childrenPlatform.add("Linux");
            childrenPlatform.add("Windows");
            childrenPlatform.add("Android");
            childrenPlatform.add("iOS");
            mChildrenData.add(childrenPlatform);
            
            List<String> childrenLanguage = new ArrayList<String>();
            childrenLanguage.add("C++");
            childrenLanguage.add("Java");
            childrenLanguage.add("Python");
            childrenLanguage.add("Lisp");
            childrenLanguage.add("Ruby");
            mChildrenData.add(childrenLanguage);
            
            List<String> childrenDesignPattern = new ArrayList<String>();
            childrenDesignPattern.add("Singleton");
            childrenDesignPattern.add("Strategy");
            childrenDesignPattern.add("Bridge");
            childrenDesignPattern.add("Command");
            childrenDesignPattern.add("Factory");
            childrenDesignPattern.add("Adapter");
            mChildrenData.add(childrenDesignPattern);
        }
        
        @Override
        public int getGroupCount() {
            return mGroupData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (mChildrenData.size() > groupPosition) {
                return mChildrenData.get(groupPosition).size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChildrenData.get(groupPosition).get(childPosition);
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
                view.setText(mGroupData.get(groupPosition));
               
            } else {
                view = createView(mGroupData.get(groupPosition), true);
            } 

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            TextView view = null;
            if (convertView != null) {
                view = (TextView)convertView;
                view.setText(mChildrenData.get(groupPosition).get(childPosition));
            } else {
                view = createView(mChildrenData.get(groupPosition).get(childPosition), false);
            }
            
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        
        public int getFirstDeletableItemPos() {
            int pos = 1;
            
            if (!mChildrenData.isEmpty()) {
                if (mChildrenData.get(0).isEmpty()) {
                    pos = 0;
                }
            }
            
            return pos;
        }
        
        public void deleteItem() {
            if (mGroupData.isEmpty()) {
                return;
            } else if (mChildrenData.get(0).isEmpty()) {
                mGroupData.remove(0);
                mChildrenData.remove(0);
            } else {
                mChildrenData.get(0).remove(0);
            }
            notifyDataSetChanged();
        }
        
        private TextView createView(String text, boolean isGroup) {
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(      
                    ViewGroup.LayoutParams.MATCH_PARENT, 80);      
            TextView myText = new TextView(ListItemFlyInOutActivity.this);      
            myText.setLayoutParams(layoutParams);      
            myText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);      
            myText.setPadding(80, 0, 0, 0);      
            myText.setText(text);    
            if (isGroup) {
                myText.setBackgroundColor(Color.parseColor("#B0D1CA"));
            } else {
                myText.setBackgroundColor(Color.parseColor("#76C2E9"));
            }
            return myText;    
        }
    }
}
