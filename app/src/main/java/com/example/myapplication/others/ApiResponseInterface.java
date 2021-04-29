package com.example.myapplication.others;

import com.android.volley.VolleyError;

public interface ApiResponseInterface {
    void isError(VolleyError volleyError, int ServiceCode);
    void isSuccess(String response, int ServiceCode);

}
