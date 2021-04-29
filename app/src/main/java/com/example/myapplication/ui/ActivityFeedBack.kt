package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.adapter.FeedBackAdapter
import com.example.myapplication.model.ChatData
import com.example.myapplication.model.Message
import com.example.myapplication.model.ServeyArray
import com.example.myapplication.others.ApiManager
import com.example.myapplication.others.ApiRequestCodes
import com.example.myapplication.others.ApiResponseInterface
import com.example.myapplication.others.Constants
import com.example.myapplication.others.Constants.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ActivityFeedBack : AppCompatActivity() {
    var apiManager: ApiManager? = null
    var apiInterface: ApiResponseInterface? = null
    var productitems: ArrayList<String> = arrayListOf()
    var product_ratings: ArrayList<Int> = arrayListOf()
    val gson = Gson()
    var etOtherFeedBack: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        callSetUpNetwork()
        for (i in 0 until arrayListFeedBack.size) {
            productitems.add(arrayListFeedBack[i].question.toString())
            product_ratings.add(-1)
        }

        var listView = findViewById<ListView>(R.id.listView)
        etOtherFeedBack = findViewById<EditText>(R.id.etOtherFeedBack)
        var tvSubmit = findViewById<Button>(R.id.tvSubmit)
        val myListAdapter = FeedBackAdapter(this, productitems, product_ratings)
        listView.adapter = myListAdapter

        tvSubmit.setOnClickListener(View.OnClickListener {
            if (product_ratings.contains(-1)) {
                Toast.makeText(this@ActivityFeedBack, "All fields are mandatory", Toast.LENGTH_LONG).show()
            } else {
                setFeedbackData()
            }
        })

        /*listView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
//            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
            val serverResponse = gson.fromJson<ServeyArray>(FeedBackData, ServeyArray::class.java)
        }*/
    }

    private fun setFeedbackData() {
        val serverResponse = gson.fromJson<ServeyArray>(FeedBackData, ServeyArray::class.java)
        val feedBackArray = JSONArray()
        var rating = 0
        try {
            for (i in 0 until productitems.size) {
                val feedBackObject = JSONObject()
                feedBackObject.put("questionId", serverResponse.serveyQuestionList[i].questionId)
                feedBackObject.put("question", serverResponse.serveyQuestionList[i].question)
                if (product_ratings[i] == 0) {
                    rating = 1
                } else if (product_ratings[i] == 1) {
                    rating = 4
                } else if (product_ratings[i] == 2) {
                    rating = 6
                } else if (product_ratings[i] == 3) {
                    rating = 7
                } else if (product_ratings[i] == 4) {
                    rating = 9
                } else if (product_ratings[i] == 5) {
                    rating = 12
                }
                feedBackObject.put("rating", rating)
                feedBackObject.put("type", serverResponse.serveyQuestionList[i].type)
                feedBackArray.put(feedBackObject)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        callApiFeedback(feedBackArray)
    }

    private fun callApiFeedback(finalJson: JSONArray) {
        val serverResponse = gson.fromJson(FeedBackData, ServeyArray::class.java)
        val params: MutableMap<String, Any> = java.util.HashMap()
        params["survey_id"] = serverResponse.surveyId
        params["survey_name"] = serverResponse.surveyName
        params["emailId"] = userEmail
        params["comment"] = etOtherFeedBack!!.text.toString()
        params["timestamp"] = System.currentTimeMillis().toLong()
        params["serveyAnswerList"] = finalJson

        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"
        apiManager!!.callPostApiWithRequestBody(Constants.apiSubmitSurvey, params, ApiRequestCodes.requestCode_Feedback, ApiRequestCodes.apimethod[1], header, true)
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
                    when (ServiceCode) {
                        ApiRequestCodes.requestCode_Feedback -> try {
                            val serverResponse = gson.fromJson<ChatData>(response, ChatData::class.java)
                            if (serverResponse.getStatus().equals("200")) {
                                Toast.makeText(this@ActivityFeedBack, "Thank you", Toast.LENGTH_LONG).show()
                                finish()
                            } else {
                                Toast.makeText(this@ActivityFeedBack, "Try again later", Toast.LENGTH_LONG).show()
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
}