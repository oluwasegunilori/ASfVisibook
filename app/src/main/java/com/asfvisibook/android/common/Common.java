package com.asfvisibook.android.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by SHEGZ on 12/30/2017.
 */
public class Common {
    public static final String SHARED_PREFERENCES_FILE_NAME_USER = "User_Details" ;
    public static final String STORAGE_PATH_UPLOADS = "certificatepdfs/";
    public static final int CAMERA_REQUEST_CODE = 4;
    public static final int PERMISSIONS_READ_CALENDAR = 107;
    public static final int PERMISSIONS_WRITE_CALENDAR = 108;
    public static final String SHARED_PREFERENCES_FILE_NAME = "Belief" ;
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 100;
    public static final int PICK_IMAGE_REQUEST = 71;

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!= null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for(int i= 0;i<info.length; i++){

                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return  true;
                    }
                }
            }
        }
    return  false;
    }



}
