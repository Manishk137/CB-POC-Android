package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    static final String TAG = AppController.class.getSimpleName();
    private static RequestQueue mRequestQueue;

    private AppController() {
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Context context, Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(context).add(req);
    }

    /*public static void addToRequestQueue(StringRequest strReq, String tag_string_req) {
        strReq.setTag(TextUtils.isEmpty(tag_string_req) ? TAG : tag_string_req);
        getRequestQueue(context).add(strReq);
    }*/

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}