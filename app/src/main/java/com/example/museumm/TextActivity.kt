package com.example.museumm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_text.*

class TextActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        btn_submit.setOnClickListener{
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}