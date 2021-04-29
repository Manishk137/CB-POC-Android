package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.myapplication.R
import com.example.myapplication.model.ChatData
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

class ActivityServey : AppCompatActivity() {
    private var apiManager: ApiManager? = null
    private var apiInterface: ApiResponseInterface? = null
    private var productitems: ArrayList<String> = arrayListOf()
    private var product_ratings: ArrayList<Int> = arrayListOf()
    private var gson = Gson()
    private var etOtherFeedBack: EditText? = null
    private var ratingBar: RatingBar? = null
    private var current_question: Int? = 0
    private var total_question: Int? = 0
    private var rateValue: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servey)
        callSetUpNetwork()
        for (i in 0 until arrayListFeedBack.size) {
            productitems.add(arrayListFeedBack[i].question.toString())
            product_ratings.add(-1)
        }

        var question_count = findViewById<TextView>(R.id.question_count)
        var question = findViewById<TextView>(R.id.question)
        var status_image = findViewById<ImageView>(R.id.status_image)
        var status_rating = findViewById<TextView>(R.id.status_rating)
        ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        etOtherFeedBack = findViewById(R.id.etOtherFeedBack)
        var btn_back = findViewById<Button>(R.id.btn_back)
        var btn_next = findViewById<Button>(R.id.btn_next)
        var tvSubmit = findViewById<Button>(R.id.btn_submit)
        ratingBar!!.numStars = 6
        ratingBar!!.stepSize = (1.0F)
        total_question = arrayListFeedBack.size
        current_question = 0
        question_count.text = "Questions 1/$total_question"
        question.text = arrayListFeedBack[0].question

        btn_back.setOnClickListener(View.OnClickListener {
            tvSubmit.visibility = View.GONE
            if (current_question!! != 0) {
                current_question = current_question!! - 1
                question_count.text = "Questions " + (current_question!! + 1) + "/" + total_question
                question.text = arrayListFeedBack[current_question!!].question
            } else {
                Toast.makeText(this, "This is first question", Toast.LENGTH_SHORT).show()
            }
            if (product_ratings[current_question!!] == 0) {
                ratingBar!!.rating = 0.0F;
                status_image.setImageResource(R.drawable.r_normal);
                status_rating.text = "Please rate us"
            } else {
                ratingBar!!.rating = product_ratings[current_question!!].toFloat()
                /*if (product_ratings[current_question!!] == 1) {
                    status_image.setImageResource(R.drawable.r_strongly_disagree_one);
                    status_rating.text = "Strongly Disagree"
                } else if (product_ratings[current_question!!] == 2) {
                    status_image.setImageResource(R.drawable.r_disagree_two);
                    status_rating.text = "Disagree"
                }*/
                when {
                    product_ratings[current_question!!] == 1 -> {
                        status_image.setImageResource(R.drawable.r_strongly_disagree_one);
                        status_rating.text = "Strongly Disagree"
                    }
                    product_ratings[current_question!!] == 2 -> {
                        status_image.setImageResource(R.drawable.r_disagree_two);
                        status_rating.text = "Disagree"
                    }
                    product_ratings[current_question!!] == 3 -> {
                        status_image.setImageResource(R.drawable.r_omewhat_disagree_three);
                        status_rating.text = "Somewhat Disagree"
                    }
                    product_ratings[current_question!!] == 4 -> {
                        status_image.setImageResource(R.drawable.r_somewhat_agree_four);
                        status_rating.text = "Somewhat Agree"
                    }
                    product_ratings[current_question!!] == 5 -> {
                        status_image.setImageResource(R.drawable.r_agree_five);
                        status_rating.text = "Agree"
                    }
                    product_ratings[current_question!!] == 6 -> {
                        status_image.setImageResource(R.drawable.r_strongly_agree_six);
                        status_rating.text = "Strongly Agree"
                    }
                }
            }
        })

        btn_next.setOnClickListener(View.OnClickListener {
            if (current_question!! < total_question!! - 1) {
                current_question = current_question!! + 1
                question_count.text = "Questions " + (current_question!! + 1) + "/" + total_question
                question.text = arrayListFeedBack[current_question!!].question
                if (current_question == total_question!! - 1) {
                    tvSubmit.visibility = View.VISIBLE
                } else {
                    tvSubmit.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "This is last question", Toast.LENGTH_SHORT).show()
            }
            if (product_ratings[current_question!!] == 0) {
//                ratingBar!!.clearFocus()
                ratingBar!!.setRating(0.0F);
                status_image.setImageResource(R.drawable.r_normal);
                status_rating.text = "Please rate us"
            } else {
                ratingBar!!.rating = product_ratings[current_question!!].toFloat()
                when {
                    product_ratings[current_question!!] == 1 -> {
                        status_image.setImageResource(R.drawable.r_strongly_disagree_one);
                        status_rating.text = "Strongly Disagree"
                    }
                    product_ratings[current_question!!] == 2 -> {
                        status_image.setImageResource(R.drawable.r_disagree_two);
                        status_rating.text = "Disagree"
                    }
                    product_ratings[current_question!!] == 3 -> {
                        status_image.setImageResource(R.drawable.r_omewhat_disagree_three);
                        status_rating.text = "Somewhat Disagree"
                    }
                    product_ratings[current_question!!] == 4 -> {
                        status_image.setImageResource(R.drawable.r_somewhat_agree_four);
                        status_rating.text = "Somewhat Agree"
                    }
                    product_ratings[current_question!!] == 5 -> {
                        status_image.setImageResource(R.drawable.r_agree_five);
                        status_rating.text = "Agree"
                    }
                    product_ratings[current_question!!] == 6 -> {
                        status_image.setImageResource(R.drawable.r_strongly_agree_six);
                        status_rating.text = "Strongly Agree"
                    }
                }
            }
        })

        ratingBar!!.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            rateValue = ratingBar.rating.toInt()
            Log.e("rating ", rateValue.toString())
            product_ratings[current_question!!] = rateValue
            when {
                product_ratings[current_question!!] == 1 -> {
                    status_image.setImageResource(R.drawable.r_strongly_disagree_one);
                    status_rating.text = "Strongly Disagree"
                }
                product_ratings[current_question!!] == 2 -> {
                    status_image.setImageResource(R.drawable.r_disagree_two);
                    status_rating.text = "Disagree"
                }
                product_ratings[current_question!!] == 3 -> {
                    status_image.setImageResource(R.drawable.r_omewhat_disagree_three);
                    status_rating.text = "Somewhat Disagree"
                }
                product_ratings[current_question!!] == 4 -> {
                    status_image.setImageResource(R.drawable.r_somewhat_agree_four);
                    status_rating.text = "Somewhat Agree"
                }
                product_ratings[current_question!!] == 5 -> {
                    status_image.setImageResource(R.drawable.r_agree_five);
                    status_rating.text = "Agree"
                }
                product_ratings[current_question!!] == 6 -> {
                    status_image.setImageResource(R.drawable.r_strongly_agree_six);
                    status_rating.text = "Strongly Agree"
                }
                else -> {
                    ratingBar!!.clearFocus()
                    status_image.setImageResource(R.drawable.r_normal);
                    status_rating.text = "Please rate us"
                }
            }
        })

        tvSubmit.setOnClickListener(View.OnClickListener {
            if (etOtherFeedBack!!.text.toString().isEmpty()) {
                Toast.makeText(this@ActivityServey, "All fields are mandatory", Toast.LENGTH_LONG).show()
            } else {
                setFeedbackData()
            }
            /* if (product_ratings.contains(-1)) {
                 Toast.makeText(this@ActivityServey, "All fields are mandatory", Toast.LENGTH_LONG).show()
             } else {
                 setFeedbackData()
             }*/
        })

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
                when {
                    product_ratings[i] == 1 -> {
                        rating = 1
                    }
                    product_ratings[i] == 2 -> {
                        rating = 4
                    }
                    product_ratings[i] == 3 -> {
                        rating = 6
                    }
                    product_ratings[i] == 4 -> {
                        rating = 7
                    }
                    product_ratings[i] == 5 -> {
                        rating = 9
                    }
                    product_ratings[i] == 6 -> {
                        rating = 12
                    }
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
                                Toast.makeText(this@ActivityServey, "Thank you", Toast.LENGTH_LONG).show()
                                finish()
                            } else {
                                Toast.makeText(this@ActivityServey, "Try again later", Toast.LENGTH_LONG).show()
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