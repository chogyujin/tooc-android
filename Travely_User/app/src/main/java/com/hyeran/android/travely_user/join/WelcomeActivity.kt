package com.hyeran.android.travely_user.join

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hyeran.android.travely_user.R
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        setClickListener()
    }


    private fun setClickListener() {
        btn_next_welcome.setOnClickListener {
            val intent = Intent(applicationContext, ExplanationActivity::class.java)
            startActivity(intent)
        }
    }
}