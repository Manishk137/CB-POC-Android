package com.example.myapplication.others;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.AppController;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ApiManager {
    private Context context;
    private ProgressDialog progressDialog;
    private ApiResponseInterface apiResponseInterface;
    int request_Timeout = 60 * 1000;

    public ApiManager(Context context, ApiResponseInterface apiResponseInterface) {
        this.context = context;
        this.apiResponseInterface = apiResponseInterface;
        progressDialog = new ProgressDialog(context);
    }

    // PARAMETERS SEND IN REQUEST BODY(JSON) FORM
    public void callPostApiWithRequestBody(String api_url, final Map<String, Object> params, final int requestCode, int method, final Map<String, String> headers, final boolean islastCall) {
        if (islastCall) {
            showDialog("please wait...");
        }
        HttpsTrustManager.allowAllSSL();
        String tag_string_req = "string_req";
        JsonObjectRequest request = new JsonObjectRequest(method, api_url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (islastCall) {
                    closeDialog();
                }
                apiResponseInterface.isSuccess(jsonObject.toString(), requestCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (islastCall) {
                    closeDialog();
                }
                apiResponseInterface.isError(volleyError, requestCode);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers.size() > 0) {
                    return headers;
                }
                return super.getHeaders();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(request_Timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.addToRequestQueue(context, request, tag_string_req);
    }

    public void callPostApiWithArrayObject(String api_url, final Map<String, Object> params, final int requestCode, int method, final Map<String, String> headers, final boolean islastCall) {
        if (islastCall) {
            showDialog("please wait...");
        }
        HttpsTrustManager.allowAllSSL();
        String tag_string_req = "string_req";
        JsonObjectRequest request = new JsonObjectRequest(method, api_url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (islastCall) {
                    closeDialog();
                }
                apiResponseInterface.isSuccess(jsonObject.toString(), requestCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (islastCall) {
                    closeDialog();
                }
                apiResponseInterface.isError(volleyError, requestCode);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers.size() > 0) {
                    return headers;
                }
                return super.getHeaders();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(request_Timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.addToRequestQueue(context, request, tag_string_req);
    }


    public void callForOtherApi(String apiurl, final Map<String,
            String> parameters, final int requestCode, int method, final Map<String, String> headers, final boolean islastCall) {
        showDialog("please wait...");
        HttpsTrustManager.allowAllSSL();
        String tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response :" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", "max");
//                params.put("password", "123456");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.addToRequestQueue(context, strReq, tag_string_req);
    }

    /**
     * The purpose of this method is to show the dialog
     *
     * @param message
     */
    private void showDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * The purpose of this method is to close the dialog
     */
    private void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static Bitmap decodeBitmapFromInputStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}