package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.ActivityGetStarted

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true)
        try {
            val sharedPref: SharedPreferences = getSharedPreferences("User_Details", 0)
            Handler().postDelayed({
                startActivity(Intent(this, ActivityGetStarted::class.java))
//                startActivity(Intent(this, ActivityChatNew2::class.java))
                finish()
               /* var strUserData = sharedPref.getString("user_data", "")
                if (strUserData?.length!! > 5) {
                    startActivity(Intent(this, ActivityChat::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, ActivityUserLogin::class.java))
                    finish()
                }*/
            }, 2000)
        } catch (e: Exception) {
        }
    }
}