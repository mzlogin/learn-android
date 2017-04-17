package org.mazhuang.bluetoothdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public DemoItem[] mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItems = new DemoItem[]{
                new DemoItem(getString(R.string.send_file_by_intent), SendFileByIntentActivity.class),
                new DemoItem(getString(R.string.run_as_server), RunAsServerActivity.class),
                new DemoItem(getString(R.string.run_as_client), RunAsClientActivity.class)
        };

        initViews();
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.demos_list);
        listView.setAdapter(new MyBaseAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, mItems[position].activityClass);
                startActivity(intent);
            }
        });
    }

    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = MainActivity.this.getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            holder.nameTextView.setText(mItems[position].name);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView nameTextView;

        ViewHolder(View parent) {
            nameTextView = (TextView) parent.findViewById(android.R.id.text1);
        }
    }

    static class DemoItem {
        String name;
        Class<?> activityClass;

        DemoItem(String name, Class<? extends Activity> clazz) {
            this.name = name;
            this.activityClass = clazz;
        }
    }
}
