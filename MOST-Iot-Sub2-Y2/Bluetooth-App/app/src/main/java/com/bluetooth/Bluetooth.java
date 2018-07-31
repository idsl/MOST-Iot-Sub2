package com.bluetooth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.a2017617iot1.HttpHandler;
import com.example.popyue.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bluetooth extends AppCompatActivity {
    private String TAG = Bluetooth.class.getSimpleName();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataHeader2;
    HashMap<String, List<String>> listDataChild;
    ArrayList<HashMap<String, String>> contactList;

    private void enableActionBarHomeButton(AppCompatActivity appCompatActivity){
        android.support.v7.app.ActionBar mActionBar = appCompatActivity.getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        enableActionBarHomeButton(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        new GetContacts().execute();
    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(Bluetooth.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://localhost:5050/bluetooth";
            String jsonStr = sh.makeServiceCall(url);
            Log.i("--進account doinback--", url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                Log.i("--進if 有收到--", "");
                try {
                    contactList = new ArrayList<HashMap<String, String>>();
                    JSONArray jsonObj = new JSONArray(jsonStr);

                    // Getting JSON Array node


                    // looping through All Contacts
                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        //String Id = c.getString("Id");
                        String dev_1 = c.getString("device1");
                        String dev_2 = c.getString("device2");
                        String conn = c.getString("connect_type");
                        String pair = c.getString("pairing_type");
                        String TK = c.getString("TK");

                        // tmp hash map for single contact
                        HashMap<String, String> bluetooth = new HashMap<>();

                        // adding each child node to HashMap key => value

                        //bluetooth.put("Id", Id);
                        bluetooth.put("device1", dev_1);
                        bluetooth.put("device2", dev_2);
                        bluetooth.put("connect_type", conn);
                        bluetooth.put("pairing_type", pair);
                        bluetooth.put("TK", TK);
                        // adding contact to contact list

                        contactList.add(bluetooth);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e(TAG, "onpost " );
            prepareListData(contactList);
            init();
        }
    }

    private void prepareListData(ArrayList<HashMap<String, String>> a) {
        listDataHeader = new ArrayList<String>();
        listDataHeader2 = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        for(int i = 0; i < a.size(); i++ ){
            listDataHeader.add("藍牙連線 "+String.valueOf(i+1));
            List<String> item = new ArrayList<String>();
            item.add("藍牙裝置A:  " + a.get(i).get("device1"));
            item.add("藍牙裝置B:  " + a.get(i).get("device2"));
            item.add("連線型態:    " + a.get(i).get("connect_type"));
            item.add("配對方法:    " + a.get(i).get("pairing_type"));
            item.add("配對金鑰:    " + a.get(i).get("TK"));
            item.add("風險程度:    " + ((i!=a.size()-1)?"無":"低"));
            listDataChild.put(listDataHeader.get(i), item);
        }
    }

    private void init(){

        listAdapter = new ExpandableListAdapter(this, listDataHeader , listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        for(int i=0; i < listAdapter.getGroupCount(); i++){
            expListView.expandGroup(i);
        }

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(id==5&&groupPosition==2){
                    Intent intent = new Intent(Bluetooth.this,Statistic_test.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
