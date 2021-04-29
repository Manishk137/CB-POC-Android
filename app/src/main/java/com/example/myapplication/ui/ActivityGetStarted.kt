package com.example.myapplication.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ActivityGetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_start)
        val sharedPref: SharedPreferences = getSharedPreferences("User_Details", 0)
        var btn_chat_start = findViewById<Button>(R.id.btn_chat_start)
        btn_chat_start.setOnClickListener {
            var strUserData = sharedPref.getString("user_data", "")
            if (strUserData?.length!! > 5) {
                startActivity(Intent(this, ActivityMenu::class.java))
            } else {
                startActivity(Intent(this, ActivityUserLogin::class.java))
            }
//            startActivity(Intent(this, ActivityChat::class.java))
//            finish()
        }

    }
}