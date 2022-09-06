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

        initTestingUI()
        initOTPView()
    }

    private fun initTestingUI(){

        copy_button.setOnClickListener {
            otp_view.copyText()
        }
        paste_button.setOnClickListener {
            otp_view.pasteText()
        }
        fill_button.setOnClickListener {
            otp_view.setText("ABCDEF_EXTRA")
        }
        clear_button.setOnClickListener {
            otp_view.clearText(false)
        }
        continue_button.setOnClickListener {
            val text = otp_view.getStringFromFields()
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun initOTPView(){
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
    }
}