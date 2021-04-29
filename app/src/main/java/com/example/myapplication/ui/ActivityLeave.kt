package com.example.myapplication.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.adapter.MyListAdapter
import com.example.myapplication.model.LeaveTypeResponse
import com.example.myapplication.model.MyListData
import com.example.myapplication.model.ServeyArray
import com.example.myapplication.model.UserRegistration
import com.example.myapplication.others.ApiManager
import com.example.myapplication.others.ApiRequestCodes
import com.example.myapplication.others.ApiResponseInterface
import com.example.myapplication.others.Constants
import com.example.myapplication.others.Constants.arrayListFeedBack
import com.example.myapplication.others.Constants.loadJSONFromAsset
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ActivityLeave : AppCompatActivity() {
    var toastView: View? = null
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null
    var leave_type: String? = ""
    var leave_date_from: String? = ""
    var leave_date_to: String? = ""
    var date_from: TextView? = null
    var date_to: TextView? = null
//    private var adapterUser: AdapterUser? = null
//    private val rvUsers: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave)

        val sharedPref: SharedPreferences = getSharedPreferences("User_Details", 0)
        var strUserData = sharedPref.getString("user_data", "")
        val gson = Gson()
        val data_type = gson.fromJson(strUserData, UserRegistration::class.java)

        leave_type = loadJSONFromAsset(this, "leave_type")

        toastView = layoutInflater.inflate(R.layout.toast_custom_view, null)

        var back_btn = findViewById<ImageView>(R.id.back_btn)
        var btn_apply_leave = findViewById<Button>(R.id.btn_apply_leave)
        callSetUpNetwork()
        back_btn.setOnClickListener {
            finish()
        }
        btn_apply_leave.setOnClickListener {
            applyLeaveLayout(this)
        }

        val myListData = arrayOf(MyListData("Sick leave"), MyListData("Casual leave"), MyListData("Maternity leave"),
                MyListData("Unpaid Leave"), MyListData("Sabbatical leave"), MyListData("Sabbatical leave"))

        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val adapter = MyListAdapter(myListData)
        recyclerView.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

    }

    private fun callApiForFeedBack() {
        val params: MutableMap<String, Any> = java.util.HashMap()
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(Constants.apiSurvey, params, ApiRequestCodes.requestCode_Survey, ApiRequestCodes.apimethod[0], header, true)
    }

    private fun callSetUpNetwork() {
        val intentFeedBack = Intent(this, ActivityFeedBack::class.java)
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
                        ApiRequestCodes.requestCode_Survey -> try {
                            arrayListFeedBack.clear()
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

    fun applyLeaveLayout(context: Context?) {
        try {
//            val dialog = Dialog(context!!)
            val dialog = Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_apply_leave)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val dialogButton: ImageView = dialog.findViewById(R.id.cancel) as ImageView
            dialogButton.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            val btn_submit_leave: Button = dialog.findViewById(R.id.btn_submit_leave) as Button
            btn_submit_leave.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            val gson = Gson()
            val serverResponse = gson.fromJson<LeaveTypeResponse>(leave_type, LeaveTypeResponse::class.java)
            val spinnerLaveItem: ArrayList<String> = ArrayList()
            for (i in 0 until serverResponse.leaveData.size) {
                spinnerLaveItem.add(serverResponse.leaveData[i].name.toString())
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerLaveItem)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val sItems = dialog.findViewById<View>(R.id.spinner_leave_type) as Spinner
            sItems.adapter = adapter

            /*half day one spinner*/
            val spinner_leave_type_half_one = dialog.findViewById<View>(R.id.spinner_leave_type_half_one) as Spinner
            var date_one = dialog.findViewById(R.id.date_one) as TextView
            val adapter_leave_one = ArrayAdapter.createFromResource(
                    this, R.array.leave_type_one, android.R.layout.simple_spinner_dropdown_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_leave_type_half_one.setAdapter(adapter_leave_one)

            /*half day two spinner*/
            val spinner_leave_type_half_two = dialog.findViewById<View>(R.id.spinner_leave_type_half_two) as Spinner
            var date_two = dialog.findViewById(R.id.date_two) as TextView
            val adapter_leave_two = ArrayAdapter.createFromResource(
                    this, R.array.leave_type_one, android.R.layout.simple_spinner_dropdown_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_leave_type_half_two.setAdapter(adapter_leave_two)

            date_from = dialog.findViewById(R.id.date_from) as TextView
            date_to = dialog.findViewById(R.id.date_to) as TextView
            var plug_switch = dialog.findViewById(R.id.plug_switch) as Switch
            var layout_full_half_day = dialog.findViewById(R.id.layout_full_half_day) as LinearLayout
            date_from!!.setOnClickListener(View.OnClickListener {
                // Get Current Date
                val c: Calendar = Calendar.getInstance()
                var mYear = c.get(Calendar.YEAR)
                var mMonth = c.get(Calendar.MONTH)
                var mDay = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth -> date_from!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay)
                datePickerDialog.show()
            })

            date_to!!.setOnClickListener(View.OnClickListener {
                // Get Current Date
                val c: Calendar = Calendar.getInstance()
                var mYear = c.get(Calendar.YEAR)
                var mMonth = c.get(Calendar.MONTH)
                var mDay = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth -> date_to!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay)
                datePickerDialog.show()
            })

            plug_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    layout_full_half_day.visibility = View.VISIBLE
                    date_one.setText(date_from!!.text)
                    date_two.setText(date_to!!.text)
                } else {
                    layout_full_half_day.visibility = View.GONE
                }
            })

            dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}