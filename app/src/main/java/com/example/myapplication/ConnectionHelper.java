package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionHelper {
    Context context;

    public ConnectionHelper(Context context) {
        this.context = context;
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public boolean pingNetwork(String ipAddress){
        boolean reachable = false;
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -w 1 "+ipAddress);
            reachable = (p1.waitFor() == 0);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Ping","e");
            reachable = false;
        }
        Log.d("Ping",reachable+"");
        return reachable;
    }
}
