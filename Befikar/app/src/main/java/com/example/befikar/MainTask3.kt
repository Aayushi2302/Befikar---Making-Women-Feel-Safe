package com.example.befikar

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_task3.*

class MainTask3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task3)

        educateCard1.setOnClickListener {
            val intent = Intent(this,EducateCard1::class.java)
            startActivity(intent)
        }

        educateCard2.setOnClickListener {
            val intent = Intent(this,EducateCard2::class.java)
            startActivity(intent)
        }

        setSupportActionBar(appBarMainTask3)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarMainTask3.setNavigationOnClickListener {
                finish()
            }
        }
    }
}