package com.example.befikar

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_educate_card2.*

class EducateCard2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educate_card2)

        val pepperSpray : TextView = findViewById(R.id.pepperSpray)
        pepperSpray.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/5EhbjYm")
            startActivity(intent)
        }

        val sharpPencil : TextView = findViewById(R.id.pepperSpray)
        sharpPencil.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/4cXDtuC")
            startActivity(intent)
        }

        val taser : TextView = findViewById(R.id.taser)
        taser.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/69q35QV")
            startActivity(intent)
        }

        val swissKnife : TextView = findViewById(R.id.swissKnife)
        swissKnife.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/c1xEeQ9")
            startActivity(intent)
        }

        val gpsTracker : TextView = findViewById(R.id.gpsTracker)
        gpsTracker.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/dEPiImV")
            startActivity(intent)
        }

        val whistle : TextView = findViewById(R.id.whistle)
        whistle.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://amzn.eu/d/c7RgjVW")
            startActivity(intent)
        }

        setSupportActionBar(appBarEducate2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarEducate2.setNavigationOnClickListener {
                finish()
            }
        }
    }
}