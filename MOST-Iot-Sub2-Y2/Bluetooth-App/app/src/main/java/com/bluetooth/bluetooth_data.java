package com.bluetooth;

/**
 * Created by taiker on 2017/7/7.
 */

public class bluetooth_data {
    private int Id;
    private String device1;
    private String device2;
    private String conn;
    private String pair;
    private String TK;

    public bluetooth_data(int Id,String device1,String device2,String conn,String pair,String TK) {
        this.Id = Id;
        this.device1 = device1;
        this.device2 = device2;
        this.conn = conn;
        this.pair = pair;
        this.TK = TK;

    }
    public int getId(){
        return Id;
    }
    public void setId(int Id){
        this.Id = Id;
    }
    public String getDevice1(){
        return device1;
    }
    public void setDevice1(String device1){
        this.device1 = device1;
    }
    public String getDevice2(){
        return device2;
    }
    public void setDevice2(String device2){
        this.device2 = device2;
    }
    public String getconn(){
        return conn;
    }
    public void setconn(String conn){
        this.conn = conn;
    }
    public String getpair(){
        return pair;
    }
    public void setpair(String pair){
        this.pair = pair;
    }
    public String getTK(){
        return TK;
    }
    public void setTK(String TK){
        this.TK = TK;
    }
}