package com.bluetooth;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017617iot1.HttpHandler;
import com.example.popyue.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistic_test extends AppCompatActivity {
    private String TAG = Statistic_test.class.getSimpleName();
    TextView statistic_text;
    LinearLayout statistic_linear;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<HashMap<String, String>> contactList;
    TextView result;
    int[] result_add = {
            R.id.statistic_1, R.id.statistic_2,
            R.id.statistic_3, R.id.statistic_4,
            R.id.statistic_5, R.id.statistic_6,
            R.id.statistic_7, R.id.statistic_8,
            R.id.statistic_9, R.id.statistic_10,
            R.id.statistic_11, R.id.statistic_12,
            R.id.statistic_13, R.id.statistic_14,
            R.id.statistic_15
    };

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
        setContentView(R.layout.activity_statistic);
        enableActionBarHomeButton(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        statistic_text = (TextView) findViewById(R.id.statistic_text);
        statistic_linear = (LinearLayout) findViewById(R.id.statistic_linear);
        init();
        //new GetContacts().execute();
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Statistic_test.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://localhost/statistic_test";
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
                        String test = c.getString("test");
                        String result = c.getString("result");

                        // tmp hash map for single contact
                        HashMap<String, String> statistic_test = new HashMap<>();

                        // adding each child node to HashMap key => value

                        //bluetooth.put("Id", Id);
                        statistic_test.put("test", test);
                        statistic_test.put("result", result);
                        // adding contact to contact list

                        contactList.add(statistic_test);

                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                //Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e(TAG, "onpost ");
            //prepareListData(contactList);
            //init();
        }
    }
    /*
    private void prepareListData(ArrayList<HashMap<String, String>> a) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        for(int i = 0; i < a.size(); i++ ){
            listDataHeader.add("傳輸資料檢測結果 "+String.valueOf(i+1));
            List<String> item = new ArrayList<String>();
            item.add("統計測試方法:  " + a.get(i).get("test"));
            item.add("測試結果:  " + a.get(i).get("result"));

            listDataChild.put(listDataHeader.get(i), item);
        }
    }
    */
    /*private void init(){

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

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
                return false;
            }
        });
    }*/

    public void init() {

        String level = "低";
        int total = 0;
        String color = "#dc3545";
        statistic_text.setBackgroundColor((int) (Long.decode("#F8D7DA") + 4278190080L));
        for (int i = 0; i < 15; i++) {
            result = (TextView) findViewById(result_add[i]);
            if (i % 5 != 0) {
                result.setText("不通過");
//                result.setText("通過");
                result.setTextColor((int) (Long.decode("#dc3545") + 4278190080L));
//                result.setTextColor((int) (Long.decode("#28a745") + 4278190080L));
            } else {
                result.setText("通過");
//                result.setText("不通過");
                result.setTextColor((int) (Long.decode("#28a745") + 4278190080L));
//                result.setTextColor((int) (Long.decode("#dc3545") + 4278190080L));
                total++;
            }
//            Log.d("loop", "" + i);
            if (i == 7 || i == 11 || i == 12) {
                result.setText("148/148 通過");
                result.setTextColor((int) (Long.decode("#28a745") + 4278190080L));
            }
            if (i == 11) {
                result.setText("8/8 通過");
                result.setTextColor((int) (Long.decode("#28a745") + 4278190080L));
            }
            if (i == 12) {
                result.setText("18/18 通過");
                result.setTextColor((int) (Long.decode("#28a745") + 4278190080L));
            }
        }
        if (total > 5) {
            level = "中";
            color = "#ffb84d";
            statistic_text.setBackgroundColor((int) (Long.decode("#FFF3CD") + 4278190080L));
        } else if (total > 10) {
            level = "高";
            color = "#28a745";
            statistic_text.setBackgroundColor((int) (Long.decode("#D4EDDA") + 4278190080L));
        }
        //statistic_linear.setBackgroundColor((int) (Long.decode("#28a745") + 4278190080L));
        statistic_text.setText(
                Html.fromHtml("不通過亂度檢測項目數：" + total +
                "/15<br>風險程度：<font color='" + color + "'>" + level)
        );
    }
}
