package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.model.ServeyArray
import com.example.myapplication.model.UserRegistration
import com.example.myapplication.others.ApiManager
import com.example.myapplication.others.ApiRequestCodes
import com.example.myapplication.others.ApiResponseInterface
import com.example.myapplication.others.Constants
import com.example.myapplication.others.Constants.*
import com.google.gson.Gson

class ActivityMenu : AppCompatActivity() {
    var toastView: View? = null
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val sharedPref: SharedPreferences = getSharedPreferences("User_Details", 0)
        var strUserData = sharedPref.getString("user_data", "")
        val gson = Gson()
        val data_type = gson.fromJson(strUserData, UserRegistration::class.java)

        toastView = layoutInflater.inflate(R.layout.toast_custom_view, null)
        var back_btn = findViewById<ImageView>(R.id.back_btn)
        var tv_name = findViewById<TextView>(R.id.tv_name)
        var layout_chat = findViewById<LinearLayout>(R.id.layout_chat)
        var layout_leave = findViewById<LinearLayout>(R.id.layout_leave)
        var layout_survey = findViewById<LinearLayout>(R.id.layout_survey)

        try {
            tv_name.setText("Hi " + data_type.getName())
            userEmail = data_type.getEmail()
        }catch (exx:Exception){

        }
        callSetUpNetwork()
        back_btn.setOnClickListener {
            finish()
        }

        layout_chat.setOnClickListener {
            if (!Constants.isInternetConnected(this@ActivityMenu)) {
                showCustomToast(getString(R.string.network_check))
            } else {
                startActivity(Intent(this, ActivityChat::class.java))
            }
        }

        layout_leave.setOnClickListener {
            if (!Constants.isInternetConnected(this@ActivityMenu)) {
                showCustomToast(getString(R.string.network_check))
            } else {
                startActivity(Intent(this, ActivityLeave::class.java))
            }
        }

        layout_survey.setOnClickListener {
            if (!Constants.isInternetConnected(this@ActivityMenu)) {
                showCustomToast(getString(R.string.network_check))
            } else {
                callApiForFeedBack()
            }
        }

    }

    private fun callApiForFeedBack() {
        val params: MutableMap<String, Any> = java.util.HashMap()
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(Constants.apiSurvey, params, ApiRequestCodes.requestCode_Survey, ApiRequestCodes.apimethod[0], header, true)
    }

    private fun callSetUpNetwork() {
        val intentFeedBack = Intent(this, ActivityServey::class.java)
        apiInterface = object : ApiResponseInterface {
            override fun isError(volleyError: VolleyError, requestcode: Int) {
                val networkResponse = volleyError.networkResponse
                if (networkResponse != null) {
                    Log.e("STATUS_CODE", networkResponse.statusCode.toString() + "")
                }
            }

            override fun isSuccess(response: String, ServiceCode: Int) {
                try {
                    FeedBackData = response
                    Log.e("response ", response)
                    val gson = Gson()
                    when (ServiceCode) {
                        ApiRequestCodes.requestCode_Survey -> try {
                            val serverResponse = gson.fromJson<ServeyArray>(response, ServeyArray::class.java)
                            arrayListFeedBack = serverResponse.getServeyQuestionList();
                            if (serverResponse.getServeyQuestionList().size > 0) {
                                startActivity(intentFeedBack)
                            } else {
                                showCustomToast(getString(R.string.try_again))
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            showCustomToast(getString(R.string.try_again))
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
}