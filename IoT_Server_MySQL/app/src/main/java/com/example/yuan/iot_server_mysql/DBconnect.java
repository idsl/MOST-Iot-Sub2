package com.example.yuan.iot_server_mysql;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Yuan on 2017/6/22.
 */


public final class DBconnect {
    static Connection connection = null;
    String checlInfo;
    public DBconnect()
    {
        Log.i("--- 進入DB-----", "0000");
        try
        {
            Log.i("--- 開始連接DB-----", "1");
            // the mysql driver string
            Class.forName("com.mysql.jdbc.Driver");
            Log.i("----檢查DB名稱-----", "2");
            // the mysql url
            String url = "jdbc:mysql://140.118.109.165:1236/iot_trust";
            Log.i("----DB進入-----", "3");
            // get the mysql database connection
            connection = DriverManager.getConnection(url,"root", "root");
            Log.i("----DB檢查-----", "4");

            // now do whatever you want to do with the connection
            // ...

        }
        catch (ClassNotFoundException e)
        {
            Log.i("----CATCH檢查-----", "5");
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e)
        {
            Log.i("----CATCH檢查-----", "6");
            e.printStackTrace();
            System.exit(2);
        }

    }
    public Connection GetConnection(){
        return connection;
    }

}
