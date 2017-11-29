package com.example.jianzhang.myapplication33;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

	public final static String TAG = "UIHelper";

	public final static int RESULT_OK = 0x00;
	public final static int REQUEST_CODE = 0x01;

	public static void ToastMessage(Context cont, String msg) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
        if(cont == null || msg <= 0) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, time).show();
	}




    public static void openActivity(Activity context, Class activityClass){
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    public static void openActivityByIntent(Activity context, Intent intent){
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);//android.R.anim.slide_in_left,android.R.anim.slide_out_right
    }


}
