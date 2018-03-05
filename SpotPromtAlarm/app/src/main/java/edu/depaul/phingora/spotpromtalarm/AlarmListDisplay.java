package edu.depaul.phingora.spotpromtalarm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListDisplay extends Activity {
    ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        list = (ListView) findViewById(R.id.list_view);
        AlarmListAdapter customAdapter = new AlarmListAdapter(getApplicationContext(),Alarms.returnList());
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarms.deleteByID(Alarms.getAlarm(position).getid());
                finish();
            }
        });

    }

    class AlarmListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        Context context;

        private String title;
        private String description;
        private ImageView image;

        public AlarmListAdapter(Context applicationContext, ArrayList<SingleAlarm> singleAlarms) {
            this.context=context;

            inflater=(LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Alarms.getLength();
        }

        @Override
        public Object getItem(int i) {
            return Alarms.getAlarm(i);
        }

        @Override
        public long getItemId(int i) {
            return i ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.alarm_list,null);

            TextView name = (TextView) convertView.findViewById(R.id.alarmlisttile);
            TextView description = (TextView) convertView.findViewById(R.id.alarmlistdescription);

            name.setText(Alarms.getAlarm(position).getName());
            description.setText(Alarms.getAlarm(position).getDescription());
            return convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}