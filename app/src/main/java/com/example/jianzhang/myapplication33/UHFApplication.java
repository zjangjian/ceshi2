package com.example.jianzhang.myapplication33;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UHFApplication extends Application {

    //add by lei.li 2016/11/12
    private static Context mContext;
    //add by lei.li 2016/11/12

    private Socket mTcpSocket = null;
    private BluetoothSocket mBtSocket = null;

    public ArrayList<CharSequence> mMonitorListItem = new ArrayList<CharSequence>();
    private static Application app;


    private List<Activity> activities = new ArrayList<Activity>();

    public static synchronized Application getInstance() {
        if (app == null) {
            app = new Application();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        mContext = getApplicationContext();


		/*CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());*/
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }


    public void setTcpSocket(Socket socket) {
        this.mTcpSocket = socket;
    }

    public void setBtSocket(BluetoothSocket socket) {
        this.mBtSocket = socket;
    }

    public static Context getContext() {
        return mContext;
    }
}
