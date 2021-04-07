package com.kevinschildhorn.otpview.sample

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otp_view.setOnFinishListener {
            Log.i("MainActivity", it)
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        otp_view.setOnCharacterUpdatedListener {
            if(it)
                Log.i("MainActivity", "The view is filled")
            else
                Log.i("MainActivity", "The view is NOT Filled")
            continue_button.isEnabled = it
            //otp_view.isFilled()
        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        otp_view.fitToWidth(width)

        fill_button.setOnClickListener {
            otp_view.setText("ABCDEF")
        }

        continue_button.setOnClickListener {
            Toast.makeText(this, otp_view.getStringFromFields(), Toast.LENGTH_LONG).show()

        }
    }
}