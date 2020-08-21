package com.kevinschildhorn.otpview.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otp_view.setOnFinishListener {
            Log.i("MainActivity",it)
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        }

        fill_button.setOnClickListener {
            otp_view.setText("ABCDEF")
        }
    }
}