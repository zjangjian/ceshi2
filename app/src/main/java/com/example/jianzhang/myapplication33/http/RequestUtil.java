package com.example.jianzhang.myapplication33.http;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.example.jianzhang.myapplication33.Constant;
import com.example.jianzhang.myapplication33.UIHelper;

import java.util.HashMap;
import java.util.Map;


public class RequestUtil extends HttpClient {


    public static boolean isRequestContentSuccess(Context context, JSONObject object) {
        boolean mIsRequestContentSuccess = false;
        try {
            if (object != null) {
                boolean result = object.getBoolean("success");
                if (result) {
                    mIsRequestContentSuccess = true;
                } else {
                    mIsRequestContentSuccess = false;
                    String msg = object.getString("msg");
                    if (msg != null)
                        UIHelper.ToastMessage(context, msg);
                }
            }
        } catch (Exception e) {
        }

        return mIsRequestContentSuccess;
    }



    public static void getUserInfo(String userid, String access_token, HttpResponseHandler httpResponseHandler) {

        String url = getRequestUrl(Constant.GET_USETINFO);
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("access_token", access_token);

        post(url, params, httpResponseHandler);
    }




    public static void uploadInventory(HttpResponseHandler httpResponseHandler) {
        String url = getRequestUrl(Constant.UPLOADDATA);

        Map<String, Object> params = new HashMap<>();

        post(url, params, httpResponseHandler);
    }


    public static String getRequestUrl(String action) {
        return Constant.DOMAIN + action;
    }
}
