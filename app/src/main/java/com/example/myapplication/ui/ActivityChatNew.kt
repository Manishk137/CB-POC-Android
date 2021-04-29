package com.example.myapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.model.ChatData
import com.example.myapplication.model.Message
import com.example.myapplication.model.UserRegistration
import com.example.myapplication.others.ApiManager
import com.example.myapplication.others.ApiRequestCodes.*
import com.example.myapplication.others.ApiResponseInterface
import com.example.myapplication.others.Constants.*
import com.example.myapplication.others.Utility.*
import com.google.gson.Gson


class ActivityChatNew : AppCompatActivity() {
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null
    var toastView: View? = null
    private var messageAdapter: MessageAdapter? = null
    private var messagesView: ListView? = null
    var editText_chat: AutoCompleteTextView? = null
    var PREF_NAME: String = "User_Details"
    var sharedPref: SharedPreferences? = null
//    var suggestions: List<String> = ArrayList()
    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_new)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPref = getSharedPreferences(PREF_NAME, 0)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        messageAdapter = MessageAdapter(this)
        messagesView = findViewById(R.id.messages_view) as ListView
        messagesView!!.setAdapter(messageAdapter)

        callSetUpNetwork()

        var back_btn = findViewById<ImageView>(R.id.back_btn)
        editText_chat = findViewById(R.id.editText_chat)
        var send_btn = findViewById<ImageButton>(R.id.send_btn)
        toastView = layoutInflater.inflate(R.layout.toast_custom_view, null)

        editText_chat!!.threshold = 1 //will start working from first character
        editText_chat!!.setTextColor(Color.RED)
//        editText_chat!!.setAdapter(adapter)

        editText_chat!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //data
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //retrieveData(s);
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 1) {
                    callSearchApi(s)
                }
            }
        })

        send_btn.setOnClickListener {
            var str_chat = editText_chat!!.text.toString()
            if (str_chat.length > 0) {
                val message = Message(editText_chat!!.text.toString(), true, System.currentTimeMillis())
                runOnUiThread {
                    messageAdapter!!.add(message)
                    messagesView!!.setSelection(messagesView!!.count - 1)
                }
//                editText_chat!!.setText("")

                var strUserData = sharedPref!!.getString("user_data", "")
                val gson = Gson()
                val serverResponse = gson.fromJson<UserRegistration>(strUserData, UserRegistration::class.java)
                //call Login API
                callChatApi(str_chat, serverResponse.getEmail().toString(), System.currentTimeMillis())
            } else {
                showCustomToast("Please write a message")
            }
        }
        back_btn.setOnClickListener {
            finish()
        }
    }

    private fun callSearchApi(key: Editable) {
        /*val params: MutableMap<String, Any> = java.util.HashMap()
        params["searchKey"] = key
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(apiSearchQuestions, params, requestCode_Search, apimethod[1], header, false)
*/

        val params: MutableMap<String, Any> = java.util.HashMap()
        params["searchKey"] = key.toString()
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(apiSearchQuestions, params, requestCode_Search, apimethod[1], header, true)
    }


    private fun callChatApi(question: String, email: String, time: Long) {
        val params: MutableMap<String, Any> = java.util.HashMap()
        params["emailId"] = email
        params["question"] = question
        params["timestamp"] = time.toLong()
        params["deviceId"] = getAndroidId(this)
        params["imei"] = getImeiNo(this)
        params["_interface"] = getInterface()
        params["model"] = getDeviceModel()
        params["macId"] = getMacAddr()
        params["version"] = getVersionName(this)
        arrayList.add(params)
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(apiChat, params, requestCode_Chat, apimethod[1], header, true)
    }

    private fun callSetUpNetwork() {
        apiInterface = object : ApiResponseInterface {
            override fun isError(volleyError: VolleyError, requestcode: Int) {
                showCustomToast(getString(R.string.try_again))
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
                        requestCode_Chat -> try {
                            val serverResponse = gson.fromJson<ChatData>(response, ChatData::class.java)
                            if (serverResponse.getStatus().equals("200")) {
                                val prodHashMap = HashMap<String, Any>()
                                prodHashMap["question"] = serverResponse.getQuestion().toString()
                                prodHashMap["flag"] = serverResponse.getAnswer().toString()
                                prodHashMap["answer"] = serverResponse.getAnswer().toString()
                                prodHashMap["msg_by"] = "HR"
                                arrayList.add(prodHashMap)
                                runOnUiThread {
                                    val message = Message(serverResponse.getAnswer().toString(), false, System.currentTimeMillis())
                                    messageAdapter!!.add(message)
                                    messagesView!!.setSelection(messagesView!!.count - 1)
                                }
                            } else {
                                showCustomToast(getString(R.string.try_again))
                            }
                        } catch (e: Exception) {
                            showCustomToast(getString(R.string.try_again))
                        }

                        requestCode_Search -> try {
                            val arrayList: ArrayList<String> = ArrayList<String>()
                            if (response.length > 10) {
                                arrayList.add("which1")
                                arrayList.add("which2")
                                arrayList.add("which3")
                                arrayList.add("which4")
                                // refresh list
                                runOnUiThread {
//                                    suggestions = arrayList
                                    adapter = ArrayAdapter(this@ActivityChatNew, android.R.layout.simple_dropdown_item_1line, arrayList)
                                    editText_chat!!.setAdapter(adapter)
//                                    adapter!!.notifyDataSetChanged()
                                }
                            }
                        } catch (e: Exception) {
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showCustomToast(getString(R.string.try_again))
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