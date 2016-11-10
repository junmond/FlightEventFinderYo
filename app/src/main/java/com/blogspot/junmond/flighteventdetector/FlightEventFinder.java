package com.blogspot.junmond.flighteventdetector;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HyunjunLee on 2016-11-09.
 */

public class FlightEventFinder {

    public static final int SIMSOCK_CONNECTED = 0;
    public static final int SIMSOCK_DATA = 1;
    public static final int SIMSOCK_DISCONNECTED = 2;
    public static SimpleSocket Socketer = null;
    Activity mainActivity;
    Context mainContext;

    public FlightEventFinder(Activity parentActivity, Context parentContext)
    {
        this.mainActivity = parentActivity;
        this.mainContext = parentContext;
    }

    public void ShowEvents(String jsonString)
    {
        ArrayList<String> eventTitleList = new ArrayList<String>();

        Log.d("JSONJUN", "try to parse json, size : " + jsonString.length());
        try
        {
            JSONArray json = new JSONArray(jsonString);
            int eventCount = json.length();
            Log.d("JSONJUN", "start enumerate json, length : " + json.length());

            for(int i=0; i<eventCount; i++)
            {
                JSONObject eventObj = json.optJSONObject(i);
                String eventTitle = eventObj.getString("EventTitle");
                String eventUrl = eventObj.getString("EventUrl");
                String eventAirline = eventObj.getString("AirLine");

                eventTitleList.add(eventObj.getString("EventTitle"));

                Log.d("JSONJUN", json.getString(i) + "title : " + eventObj.getString("EventTitle"));
            }
            Log.d("JSONJUN", "end enumerate json");
        }
        catch (Exception e)
        {
            Log.d("Hyunjun", e.toString());
        }

        ListView listView = (ListView)this.mainActivity.findViewById(R.id.lstCustom);

        listView.setAdapter(new FlightEventAdapter(mainContext, eventTitleList));
    }

    public void RefreshEvents()
    {
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch(inputMessage.what){
                    case SIMSOCK_CONNECTED :
                        String msg = (String) inputMessage.obj;
                        Log.d("OUT",  msg);
                        Log.d("Hyunjun", "SIMSOCK_CONNECTED");
                        // do something with UI

                        break;

                    case SIMSOCK_DATA :
                        // do something with UI
                        Log.d("Hyunjun", "SIMSOCK_DATA");

                        String jsonString = (String)inputMessage.obj;
                        ShowEvents(jsonString);
                        break;

                    case SIMSOCK_DISCONNECTED :
                        // do something with UI
                        Log.d("Hyunjun", "SIMSOCK_DISCONNECTED");
                        break;
                    default:
                        Log.d("Hyunjun", "unknown handler message : " + inputMessage.what);
                }
            }
        };

        Socketer = new SimpleSocket("121.130.230.62", 5007, mHandler);
        Socketer.start();
    }

    public class FlightEventAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<String> eventTitleList;

        public FlightEventAdapter(Context context, ArrayList<String> eventTitleList){
            this.inflater = LayoutInflater.from(context);
            this.eventTitleList = eventTitleList;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtEventTitle;
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.eventlist, null);
                txtEventTitle = (TextView)convertView.findViewById(R.id.txtEventTitle);
                convertView.setTag(txtEventTitle);
            }
            else
            {
                txtEventTitle = (TextView)convertView.getTag();
            }

            String eventTitle = this.eventTitleList.get(position);

            txtEventTitle.setText( eventTitle);

            return convertView;
        }

        public final int getCount() {
            return eventTitleList.size();
        }

        public final Object getItem(int position) {
            return eventTitleList.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }

        public final String getItemAtPosition(int position)
        {
            return eventTitleList.get(position);
        }

    }

}
