package com.example.myapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.model.ChatData
import com.example.myapplication.model.Message
import com.example.myapplication.model.UserRegistration
import com.example.myapplication.others.*
import com.example.myapplication.others.Constants.hideKeyboard
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ActivityChat : AppCompatActivity() {
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null
    var toastView: View? = null
    private var messageAdapter: MessageAdapter? = null
    private var messagesView: ListView? = null
    var actv: AutoCompleteTextView? = null
    var adapter: ArrayAdapter<String>? = null
    var PREF_NAME: String = "User_Details"
    var sharedPref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_new)
        sharedPref = getSharedPreferences(PREF_NAME, 0)
        callSetUpNetwork()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        messageAdapter = MessageAdapter(this)
        messagesView = findViewById(R.id.messages_view) as ListView
        messagesView!!.setAdapter(messageAdapter)
        actv = findViewById<View>(R.id.editText_chat) as AutoCompleteTextView
        //actv!!.threshold = 1 //will start working from first character

//        actv!!.setTextColor(Color.RED)

        actv!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //data
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //retrieveData(s);
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 1) {
                    callSearchApi(s.toString())
                }
            }
        })

        actv!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this@ActivityChat)
            }
            false
        })

        var back_btn = findViewById<ImageView>(R.id.back_btn)
        var send_btn = findViewById<ImageButton>(R.id.send_btn)
        toastView = layoutInflater.inflate(R.layout.toast_custom_view, null)


        send_btn.setOnClickListener {
            var str_chat = actv!!.text.toString()
            if (str_chat.length > 0) {
                val message = Message(actv!!.text.toString(), true, System.currentTimeMillis())
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

    private fun callChatApi(question: String, email: String, currentTimeMillis: Long) {
        val params: MutableMap<String, Any> = java.util.HashMap()
        params["emailId"] = email
        params["question"] = question
        params["timestamp"] = currentTimeMillis.toLong()
        params["deviceId"] = Utility.getAndroidId(this)
        params["imei"] = Utility.getImeiNo(this)
        params["_interface"] = Utility.getInterface()
        params["model"] = Utility.getDeviceModel()
        params["macId"] = Utility.getMacAddr()
        params["version"] = Utility.getVersionName(this)
        Constants.arrayList.add(params)
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(Constants.apiChat, params, ApiRequestCodes.requestCode_Chat, ApiRequestCodes.apimethod[1], header, true)
    }


    private fun callSearchApi(key: String) {
        /*val params: MutableMap<String, Any> = java.util.HashMap()
        params["searchKey"] = key
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(apiSearchQuestions, params, requestCode_Search, apimethod[1], header, false)
*/

        val params: MutableMap<String, Any> = java.util.HashMap()
        params["searchKey"] = key
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(Constants.apiSearchQuestions, params, ApiRequestCodes.requestCode_Search, ApiRequestCodes.apimethod[1], header, false)
    }

    private fun callSetUpNetwork() {
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
                        ApiRequestCodes.requestCode_Chat -> try {
                            val serverResponse = gson.fromJson<ChatData>(response, ChatData::class.java)
                            if (serverResponse.getStatus().equals("200")) {
                                val prodHashMap = HashMap<String, Any>()
                                prodHashMap["question"] = serverResponse.getQuestion().toString()
                                prodHashMap["flag"] = serverResponse.getAnswer().toString()
                                prodHashMap["answer"] = serverResponse.getAnswer().toString()
                                prodHashMap["msg_by"] = "HR"
                                Constants.arrayList.add(prodHashMap)
                                runOnUiThread {
                                    val message = Message(serverResponse.getAnswer().toString(), false, System.currentTimeMillis())
                                    messageAdapter!!.add(message)
                                    messagesView!!.setSelection(messagesView!!.count - 1)
                                }
                                actv!!.setText("")
                            } else {
                                showCustomToast(getString(R.string.try_again))
                            }
                        } catch (e: Exception) {
                            showCustomToast(getString(R.string.try_again))
                        }

                        ApiRequestCodes.requestCode_Search -> try {
                            var searchKey: ArrayList<String> = ArrayList<String>()
                            if (response.length > 10) {
                                try {
                                    //JSON is the JSON code above
                                    val jsonResponse = JSONObject(response)
                                    val cast: JSONArray = jsonResponse.getJSONArray("questions")
                                    for (i in 0 until cast.length()) {
                                        val actor = cast.getJSONObject(i)
                                        val name = actor.getString("question")
                                        searchKey.add(name)
                                    }
                                } catch (e: JSONException) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace()
                                }
                                // refresh list
                                runOnUiThread {
                                    //select_dialog_item
                                    adapter = ArrayAdapter(this@ActivityChat, android.R.layout.simple_dropdown_item_1line, searchKey)
                                    actv!!.setAdapter(adapter)
//                                    adapter!!.notifyDataSetChanged()
                                }
                            }
                        } catch (e: Exception) {
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