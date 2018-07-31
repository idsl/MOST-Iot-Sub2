package com.example.yuan.iot_server_mysql;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by taiker on 2017/7/15.
 */
public class WebService extends Service {
   // private Handler handler = new Handler();
   int vernumber = 0 ;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        AsyncHttpServer server = new AsyncHttpServer();

        List<WebSocket> _sockets = new ArrayList<WebSocket>();

        server.post("/login", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("userid", String.valueOf(((JSONObjectBody)request.getBody()).get()));
                Log.i("----123456789----","登入");
                JSONObject nodeRoot = null;
                try {
                    nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody)request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    String password = nodeRoot.getString("password");
                    Log.i("pwd", password);
                    JSONObject result = Check(userid,password);
                    Log.i("----123456789----","回傳");
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        server.post("/add", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","註冊");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody)request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    String password = nodeRoot.getString("password");
                    Log.i("pwd", password);
                    String name = nodeRoot.getString("name");
                    Log.i("name", name);
                    String resu = AddCheckUser(userid, password, name);
                    JSONObject result = new JSONObject();
                    if(resu.equals("1")){
                        SendEmail(userid);
                        Log.i("已寄送email", "已寄送email");
                        result.put("Status", "1"); // 寄發驗證信
                    }else{
                        result.put("Status", "0"); // 已有此帳號
                    }
                    response.send(String.valueOf(result ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/insert", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","新增用戶");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody)request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    JSONObject result = Insert(userid);
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/delete", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","刪除");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody)request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    String password = nodeRoot.getString("password");
                    Log.i("pwd", password);
                    JSONObject result = Delete(userid,password);
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/view", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","查看資料表");
                JSONArray result =Alltable();
                response.send(String.valueOf(result));

            }
        });
        server.post("/email", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","寄email");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody)request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    JSONObject result =SendEmail(userid);
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/emailverAdd", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----", "比對驗證碼");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody) request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    String password = nodeRoot.getString("password");
                    Log.i("pwd", password);
                    String name = nodeRoot.getString("name");
                    Log.i("name", name);
                    String captcha = nodeRoot.getString("captcha");
                    Log.i("captcha傳送過來的", captcha);
                    Log.i("應該要輸入的", String.valueOf(vernumber));
                    JSONObject result = new JSONObject();
                    if (Objects.equals(captcha, String.valueOf(vernumber))) {
                        result.put("Status", "1");//驗證通過
                        DbaddUser(userid, password, name);
                        Log.i("---比對驗證碼+新增使用者---", "驗證通過(已新增) 1");
                    }else{
                        result.put("Status", "0");//驗證未通過
                        Log.i("---比對驗證碼---", "驗證未通過 0");
                    }
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/emailverLogin", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----", "比對驗證碼");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody) request.getBody()).get()));
                    String captcha = nodeRoot.getString("captcha");
                    Log.i("captcha傳送過來的", captcha);
                    Log.i("應該要輸入的", String.valueOf(vernumber));
                    JSONObject result = new JSONObject();
                    if (Objects.equals(captcha, String.valueOf(vernumber))) {
                        result.put("Status", "1");//驗證通過
                        Log.i("---比對驗證碼---", "驗證通過 1");
                    }else{
                        result.put("Status", "0");//驗證未通過
                        Log.i("---比對驗證碼---", "驗證未通過 0");
                    }
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/devicelist", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----", "裝置列表");
                try {
                    JSONObject nodeRoot = new JSONObject(String.valueOf(((JSONObjectBody) request.getBody()).get()));
                    String userid = nodeRoot.getString("userid");
                    Log.i("userid", userid);
                    JSONObject result =AlltableDevice(userid);
                    response.send(String.valueOf(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        server.post("/deviceview", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","查看裝置資料表");
                JSONArray result = DeviceAlltable();
                response.send(String.valueOf(result));
            }
        });
        server.post("/bluetooth", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.i("----123456789----","查看裝置資料表");
                JSONArray result = bluetooth();
                response.send(String.valueOf(result));
            }
        });
// listen on port 5000
        server.listen(5050);
       // handler.postDelayed(showTime, 1000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        //handler.removeCallbacks(showTime);
        super.onDestroy();
    }

//    private Runnable showTime = new Runnable() {
//        public void run() {
//            //log目前時間
//
//            handler.postDelayed(this, 1000);
//        }
//    };
private int Number(){
    Log.i("----驗正碼產生------", "開始");
    int number = (int)(Math.random()*9000) + 1000;
    Log.i("----驗正碼產生------", String.valueOf(number));
    return number;
}


    private JSONObject Check(String Userid, String Passwd) throws JSONException{
        Log.i("----檢查-----", "123456789");
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        JSONObject jsonCheck = new JSONObject();
        String checkInfo = null, checkInfo2 = null; //初始化參數
        int Authority = 0;
        Log.i("----檢查-----", "開始");
        try {
            String sql = "SELECT * FROM User_Account WHERE UserID = ?";
            PreparedStatement stmt = DBconn.prepareStatement(sql);//資料庫查詢
            stmt.setString(1, Userid);
//            stmt.setString(2, Passwd);
            ResultSet myrs = stmt.executeQuery();
            if(myrs.next()) {
                checkInfo = myrs.getString("UserID");
                Log.i("---UserID------", checkInfo);
                checkInfo2 = myrs.getString("Password");
                Log.i("---UserID--passwd----", checkInfo2);
                Authority = myrs.getInt("Authority");
                Log.i("--UserID-Authority --", String.valueOf(Authority));
            }
            stmt.close();
            myrs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (checkInfo == null) {
            Log.i("--------3-", "需要註冊");
            jsonCheck.put("Status", "null");
            return jsonCheck;
        } else {
            if (Authority == 1) {
                Log.i("-----123----", "他是管理者");
                jsonCheck.put("Status", "1");
                return jsonCheck;
            } else if (checkInfo2.equals(Passwd)) {
                Log.i("-------1-", "密碼正確");
                jsonCheck.put("Status", "0");
                return jsonCheck;
            } else {
                Log.i("-------2-", "密碼錯誤");
                jsonCheck.put("Status", "error");
                return jsonCheck;
            }
        }
    }

    private String AddCheckUser(String Userid, String Passwd, String Name){
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();
        String checkInfo = null;
        PreparedStatement stmtAdd = null;
        String StringAdd= null;
        try {
            String sql = "SELECT * FROM User_Account WHERE UserID = ?";
            stmtAdd = DBconn.prepareStatement(sql);//資料庫查詢
            stmtAdd.setString(1, Userid);
            ResultSet myrs = stmtAdd.executeQuery();
            ResultSetMetaData resultSetMateData = myrs.getMetaData();     //獲得列的相關信息
            int cols_len = resultSetMateData.getColumnCount();            //獲得列的長度
            Log.i("---cols_len------", String.valueOf(cols_len));
            Log.i("---myrs.next------", String.valueOf(myrs.next()));
            if(myrs.next() == false) {
                StringAdd = "1";//需要註冊
                Log.i("---註冊------", "需要註冊 0" );
            }else {
                for (int i = 0; i < cols_len; i++) { //拿取資料庫的資料
                    checkInfo = myrs.getString("UserID");
                    Log.i("---UserID------", checkInfo);
                    StringAdd = "0";//已有帳號
                    Log.i("---註冊------", "Status 0" );
                }
            }
            stmtAdd.close();
            myrs.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return StringAdd;
    }

    private JSONObject DbaddUser (String Userid, String Passwd, String Name){
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();
        int Authority = 0;
        PreparedStatement stmtdb  = null;
        JSONObject jsondb = new JSONObject();
        Log.i("--Dbadd------", "開始新增使用者");
        try {
            String sql2 ="INSERT INTO User_Account(UserID,Password,Authority,Name) " +
                    " VALUES (?,?,?,?)";
            Log.i("--sql2------", sql2 );
            stmtdb = DBconn.prepareStatement(sql2);//資料庫查詢
            stmtdb .setString(1, Userid);
            stmtdb .setString(2, Passwd);
            stmtdb .setInt(3, Authority);
            stmtdb .setString(4, Name); //還要嗎?!
            stmtdb.executeUpdate( );
            stmtdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Log.i("--sql2------", "try");
            jsondb.put("Status","1");//完成加入
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsondb;
    }

    private JSONObject Insert(String Userid){
        Log.i("--Insert------", "Insert");
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();
        String checkInfo = null;
        int Authority = 0;
        PreparedStatement stmtInsert = null;
        JSONObject jsonInsert = new JSONObject();
        try {
            String sql3 = "SELECT * FROM Home_Authority WHERE UserID = ?";
            stmtInsert = DBconn.prepareStatement(sql3);//資料庫查詢
            stmtInsert.setString(1, Userid);
            ResultSet myrs = stmtInsert.executeQuery();
            ResultSetMetaData resultSetMateData = myrs.getMetaData();     //獲得列的相關信息
            int cols_len = resultSetMateData.getColumnCount();            //獲得列的長度
            if(myrs.next() == false) {
                Log.i("--Insert------", "Home_Authority沒有此人");
                String sqlsearch = "SELECT * FROM User_Account WHERE UserID = ?";
                stmtInsert = DBconn.prepareStatement(sqlsearch);//資料庫查詢
                stmtInsert.setString(1, Userid);
                ResultSet myrs1 = stmtInsert.executeQuery();
                if(myrs1.next() == false){
                    Log.i("--Insert------", "db沒有使用者");
                    try {
                        jsonInsert.put("Status","error");//沒有此使用者
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        String sql4 ="INSERT INTO Home_Authority(UserID,Authority) " +
                                " VALUES (?,?)";
                        stmtInsert = DBconn.prepareStatement(sql4);//資料庫查詢
                        stmtInsert.setString(1, Userid);
                        stmtInsert.setInt(2, Authority);
                        stmtInsert.executeUpdate( );
                        stmtInsert.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.i("--Insert------", "加入成功");
                        jsonInsert.put("Status","1");//完成加入
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                for (int i = 0; i < cols_len; i++) { //拿取資料庫的資料
                    checkInfo = myrs.getString("UserID");
                    Log.i("---UserID------", checkInfo);
                }
                stmtInsert.close();
                myrs.close();
                try {
                    Log.i("--Insert------", "已有權限");
                    jsonInsert.put("Status", "0");//已有帳號
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonInsert;
    }

    private JSONObject Delete(String Userid, String Passwd){
        Log.i("----DELETE-----",Userid);
        Log.i("----DELETE-----",Passwd);
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        Log.i("----DELETE-----", "開始");
        PreparedStatement stmtDelete = null;

        try {
            String sql = "DELETE FROM Home_Authority WHERE UserID = ?";
            stmtDelete = DBconn.prepareStatement(sql);// 刪除資料欄
            stmtDelete.setString(1, Userid);
            ResultSet myrsDelete= stmtDelete.executeQuery(); //執行刪除
            stmtDelete.close();
            myrsDelete.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonDelete = new JSONObject();
        try {
            jsonDelete.put("Status","1"); //完成刪除
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonDelete ;
    }

    private JSONArray DeviceAlltable(){ //裝置列表
        JSONArray JDArray=new JSONArray();
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        String checkInfo = null, checkInfo2 = null; //初始化參數
        Log.i("----Home_Authority----", "開始");
        try {
            String sql = "SELECT Name FROM device_value ";
            PreparedStatement stmtAll = DBconn.prepareStatement(sql);//資料庫查詢
            ResultSet myrsAll = stmtAll.executeQuery();
            Log.i("---- device_value----", String.valueOf(myrsAll));
            while (myrsAll.next()) { // 一個一個列出資料
                JSONObject jsonAlltable = new JSONObject();
                checkInfo2 = myrsAll.getString("Name");
                Log.i("---Name----", checkInfo2);
                try {   //轉成JSON格式
                    jsonAlltable.put("Name", checkInfo2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JDArray.put(jsonAlltable);
            }
            Log.i("- device_value-count-", String.valueOf(JDArray));
            stmtAll.close();
            myrsAll.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  JDArray ;
    }

    private JSONArray Alltable(){
        JSONArray JArray=new JSONArray();
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        String checkInfo = null, checkInfo2 = null; //初始化參數
        Log.i("----Home_Authority----", "開始");
        try {
            String sql = "SELECT UserID,Name FROM Home_Authority ";
            PreparedStatement stmtAll = DBconn.prepareStatement(sql);//資料庫查詢
            ResultSet myrsAll = stmtAll.executeQuery();
            Log.i("----Home_myrsAll----", String.valueOf(myrsAll));
            while (myrsAll.next()) { // 一個一個列出資料
                JSONObject jsonAlltable = new JSONObject();
                checkInfo = myrsAll.getString("UserID");
                Log.i("---UserID------", checkInfo);
                checkInfo2 = myrsAll.getString("Name");
                Log.i("---Name----", checkInfo2);
                try {   //轉成JSON格式
                    jsonAlltable.put("UserID", checkInfo);
                    jsonAlltable.put("Name", checkInfo2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JArray.put(jsonAlltable);
            }
            Log.i("-Home_Authority-count-", String.valueOf(JArray));
            stmtAll.close();
            myrsAll.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  JArray ;
    }

    private JSONObject AlltableDevice(String Userid){
        Log.i("----View-----",Userid);
        int count = 0;
        JSONObject jsonAllDevice = new JSONObject();
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        String checkInfo = null, checkInfo2 = null; //初始化參數
        Log.i("----Home_Authority----", "開始");
        try {
            String sql = "SELECT UserID,Name FROM Home_Authority ";
            PreparedStatement stmtAll = DBconn.prepareStatement(sql);//資料庫查詢
            ResultSet myrsAll = stmtAll.executeQuery();
            Log.i("----Home_myrsAll----", String.valueOf(myrsAll));
            while (myrsAll.next()) {
                checkInfo = myrsAll.getString("UserID");
                Log.i("---UserID------", checkInfo);
                checkInfo2 = myrsAll.getString("Name");
                Log.i("---Name----", checkInfo2);
                count++;
            }
            Log.i("-Home_Authority-count-", String.valueOf(count));
            stmtAll.close();
            myrsAll.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count>0){    // 若有資料
            String str="總共有 "+count+"筆資料 ";
            Log.i("----View--if---",str);
            try {   //轉成JSON格式
                jsonAllDevice.put("Status",str);
                jsonAllDevice.put("UserID",checkInfo);
                jsonAllDevice.put("Name",checkInfo2);
                Log.i("----View--UserID---",checkInfo);
                Log.i("----View--Name---",checkInfo2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonAllDevice ;
    }

    //project2      insert
    private JSONArray bluetooth(){
        int count = 0,Id=0;
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線

        JSONArray bluetooth_list = new JSONArray();

        String dev_1 = null, dev_2 = null, conn = null, pair = null ,TK = null;
        try {
            String sql = "SELECT * FROM bluetooth_report";
            PreparedStatement stmtAll = DBconn.prepareStatement(sql);//資料庫查詢
            ResultSet myrsAll = stmtAll.executeQuery();

            //Log.i("----Home_myrsAll----", String.valueOf(myrsAll));
            while (myrsAll.next()) {
                JSONObject bluetooth = new JSONObject();
                Id = myrsAll.getInt("id");
                dev_1 = myrsAll.getString("device1");
                dev_2 = myrsAll.getString("device2");
                conn = myrsAll.getString("connect_type");
                pair = myrsAll.getString("pairing_type");
                TK = myrsAll.getString("TK");
                try {   //轉成JSON格式
                    bluetooth.put("Id", Id);
                    bluetooth.put("device1", dev_1);
                    bluetooth.put("device2", dev_2);
                    bluetooth.put("connect_type", conn);
                    bluetooth.put("pairing_type", pair);
                    bluetooth.put("TK", TK);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bluetooth_list.put(bluetooth);
            }
            Log.i("-Home_Authority-count-", String.valueOf(bluetooth_list));
            stmtAll.close();
            myrsAll.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bluetooth_list;
    }

    private JSONObject SendEmail(String Userid) {
        Log.i("----SendEmail-----", "123456789");
        DBconnect myDB = new DBconnect();
        Connection DBconn = myDB.GetConnection();//建立連線
        JSONObject jsonEmail = new JSONObject();
        String checkInfo = null, checkInfo2 = null; //初始化參數
        Log.i("----SendEmail------", "開始");
        try {
            String sql = "SELECT * FROM User_Account WHERE UserID = ?";
            PreparedStatement stmt = DBconn.prepareStatement(sql);//資料庫查詢
            stmt.setString(1, Userid);
//            stmt.setString(2, Passwd);
            ResultSet myrs = stmt.executeQuery();
            vernumber = Number();
            String content ="您的驗證碼是： "+ vernumber;
            Log.i("----驗正碼產生------",content);
            GmailSender sender = new GmailSender("ntust.islab.iot.1@gmail.com", "2017islabiot01");
            sender.sendMail("可信任架構平台驗證信", content, "ntust.islab.iot.1@gmail.com",Userid);//"主旨", "內容", "寄件者", "收件者"
            stmt.close();
            myrs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonEmail.put("Status","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("----SendEmail-------","已完成寄信動作");
        return jsonEmail;
    }

}
