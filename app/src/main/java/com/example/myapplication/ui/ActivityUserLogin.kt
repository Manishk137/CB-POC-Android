package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.model.UserRegistration
import com.example.myapplication.others.ApiManager
import com.example.myapplication.others.ApiRequestCodes.apimethod
import com.example.myapplication.others.ApiRequestCodes.requestCode_login
import com.example.myapplication.others.ApiResponseInterface
import com.example.myapplication.others.Constants
import com.example.myapplication.others.Constants.apiLogin
import com.example.myapplication.others.Constants.isInternetConnected
import com.google.gson.Gson

class ActivityUserLogin : AppCompatActivity() {

    var PREF_NAME: String = "User_Details"
    var sharedPref: SharedPreferences? = null
    var toastView: View? = null
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        callSetUpNetwork()
    }

    private fun initView() {
        sharedPref = getSharedPreferences(PREF_NAME, 0)
        // get reference to all views
        var back_btn = findViewById<ImageView>(R.id.back_btn)
        var et_user_id = findViewById<EditText>(R.id.edit_text_user_id)
        var et_user_password = findViewById<EditText>(R.id.edit_text_password)
        var btn_sign_in = findViewById<Button>(R.id.btn_sign_in)

        toastView = layoutInflater.inflate(R.layout.toast_custom_view, null)

        back_btn.setOnClickListener {
            finish()
        }

        btn_sign_in.setOnClickListener {
            var strId = et_user_id.text.toString()
            var strUserPassword = et_user_password.text.toString()
            if (TextUtils.isEmpty(strId)) {
                showCustomToast(getString(R.string.user_id))
//                Toast.makeText(this@ActivityLogin, "Please Enter User Id", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(strUserPassword)) {
                showCustomToast(getString(R.string.user_pass))
            } else if (!isInternetConnected(this@ActivityUserLogin)) {
                showCustomToast(getString(R.string.network_check))
            } else {
                //call Login API
                callConnectToLogin(strId, strUserPassword)
            }
        }
    }

    private fun callConnectToLogin(emailId: String, password: String) {
        val params: MutableMap<String, Any> = java.util.HashMap()
        params["emailId"] = emailId
        params["ldap"] = password
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(apiLogin, params, requestCode_login, apimethod[1], header, true)
    }

    private fun callSetUpNetwork() {
        val intentChat = Intent(this, ActivityMenu::class.java)
        apiInterface = object : ApiResponseInterface {
            override fun isError(volleyError: VolleyError, requestcode: Int) {
                val networkResponse = volleyError.networkResponse
                if (networkResponse != null) {
                    Log.e("STATUS_CODE", networkResponse.statusCode.toString() + "")
                }
            }

            override fun isSuccess(response: String, ServiceCode: Int) {
                try {
                    Log.e("response ", response)
                    val gson = Gson()
                    when (ServiceCode) {
                        requestCode_login -> try {
                            val serverResponse = gson.fromJson<UserRegistration>(response, UserRegistration::class.java)
                            if (serverResponse.getStatus().equals("200")) {
//                                intentChat.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intentChat)
                                finish()
                                val editor = sharedPref?.edit()
                                editor?.putString("user_data", response)
                                editor?.apply()
                            } else if (serverResponse.getStatus().equals("201")) {
                                showCustomToast(getString(R.string.not_registr))
//                                startActivity(intentChat)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        apiManager = ApiManager(this, apiInterface)
    }

    fun showCustomToast(strMsg: String) {
        customShowToast(this, strMsg, toastView)
    }

    fun customShowToast(context: Context?, msg: String?, toastView: View?) {
        try {
            val toast = Toast(context)
            toast.view = toastView
            val customToastText = toastView!!.findViewById<TextView>(R.id.customToastText)
            toast.duration = Toast.LENGTH_LONG
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            customToastText.text = msg
            toast.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

//    private fun vibrate() {
//        val vibrate = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
//        } else {
//            vibrate.vibrate(100)
//        }
//    }

}
