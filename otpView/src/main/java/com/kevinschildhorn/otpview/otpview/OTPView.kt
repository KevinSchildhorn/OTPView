/*
    MIT License

    Copyright (c) 2020 Kevin Schildhorn

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */

package com.kevinschildhorn.otpview.otpview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.otp_view_layout.view.*


class OTPView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    // All
    private val itemCount:Int
    private val showCursor:Boolean
    private val inputType:Int
    private val itemWidth:Int
    private val itemHeight:Int
    private val cursorColor:Int
    private val allCaps:Boolean
    private val marginBetween:Int
    private val isPassword:Boolean

    // Default

    private val textSizeDefault:Int
    private val textColor:Int
    private val backgroundImage:Drawable?
    private val font:Typeface?

    // Highlighted

    private val highlightedTextSize:Int
    private val highlightedTextColor:Int
    private val highlightedBackgroundImage:Drawable?
    private val highlightedFont:Typeface?

    // Filled

    private val filledTextSize:Int
    private val filledTextColor:Int
    private val filledBackgroundImage:Drawable?
    private val filledFont:Typeface?



    private var onFinishFunction: ((String) -> Unit) = {}
    private val editTexts:MutableList<EditText> = mutableListOf()
    private var focusIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.otp_view_layout, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OTPView,
            0, 0)
        .apply {
            try {
                itemCount = getInteger(R.styleable.OTPView_otp_itemCount, 1)
                showCursor = getBoolean(R.styleable.OTPView_otp_showCursor, false)
                inputType = getInteger(R.styleable.OTPView_android_inputType, 0)
                itemWidth = getDimensionPixelSize(R.styleable.OTPView_otp_itemWidth, 44)
                itemHeight = getDimensionPixelSize(R.styleable.OTPView_otp_itemHeight, 44)
                cursorColor = getColor(R.styleable.OTPView_otp_cursorColor, Color.BLACK)
                allCaps = getBoolean(R.styleable.OTPView_otp_allcaps, false)
                marginBetween = getDimensionPixelSize(R.styleable.OTPView_otp_marginBetween, 8.dpTopx)
                isPassword = getBoolean(R.styleable.OTPView_otp_ispassword, false)

                textSizeDefault = getDimensionPixelSize(R.styleable.OTPView_otp_textSize, 14.dpTopx)
                textColor = getInteger(R.styleable.OTPView_otp_textColor, Color.BLACK)
                backgroundImage = getDrawable(R.styleable.OTPView_otp_backgroundImage) ?: customBackground()
                font = getFont(R.styleable.OTPView_otp_Font)

                highlightedTextSize = getDimensionPixelSize(R.styleable.OTPView_otp_highlightedTextSize, textSizeDefault)
                highlightedTextColor = getInteger(R.styleable.OTPView_otp_highlightedTextColor, textColor)
                highlightedBackgroundImage = getDrawable(R.styleable.OTPView_otp_highlightedBackgroundImage) ?: backgroundImage
                highlightedFont = getFont(R.styleable.OTPView_otp_highlightedFont) ?: font

                filledTextSize = getDimensionPixelSize(R.styleable.OTPView_otp_filledTextSize, textSizeDefault)
                filledTextColor = getInteger(R.styleable.OTPView_otp_filledTextColor, textColor)
                filledBackgroundImage = getDrawable(R.styleable.OTPView_otp_filledBackgroundImage) ?: backgroundImage
                filledFont = getFont(R.styleable.OTPView_otp_filledFont) ?: font

                initEditTexts()
            } finally {
                recycle()
            }
        }
    }

    private var disableEditListener:Boolean = false

    // Init

    private fun initEditTexts(){
        for(x in 0 until itemCount){
            addEditText()
            addListenerForIndex(x)
        }

        styleEditTexts()
        val et = editTexts[0]
        et.postDelayed(Runnable {
            val et = editTexts[focusIndex]
            et.requestFocus()
            styleEditTexts()
            showKeyboard(true, et)
        }, 100)
    }

    private fun addListenerForIndex(index:Int){
        editTexts[index].addTextChangedListener {
            if(!disableEditListener) {
                // Only Taking the last char
                if (editTexts[index].text.length > 1) {
                    editTexts[index].setText(it?.first().toString() ?: "")
                } else {
                    focusIndex = index + 1
                    if (index + 1 < editTexts.size) {
                        // Change focus to next edit text
                        editTexts[focusIndex].requestFocus()
                    } else {
                        // Clear all focus and hide keyboard
                        var str = ""
                        editTexts.forEach {
                            str += it.text.first()
                            it.clearFocus()
                        }
                        showKeyboard(false,editTexts.last())

                        onFinishFunction(str)
                    }
                    styleEditTexts()
                }
            }
        }
    }

    private fun addEditText(){
        val et = EditText(context)

        // All

        et.isCursorVisible = showCursor
        et.inputType = inputType
        val params = LayoutParams(
            itemWidth,
            itemHeight
        )

        et.isAllCaps = allCaps
        params.setMargins(
            0,
            8.dpTopx,
            marginBetween,
            8.dpTopx
        )
        et.layoutParams = params
        et.gravity = Gravity.CENTER
        //et.filters = arrayOf<InputFilter>(LengthFilter(1))

        // Default
        styleDefault(et)

        et.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                et.post(Runnable { et.setSelection(0) })
            }
        }

        editTexts.add(et)
        otp_wrapper.addView(et)
    }


    // Styling

    private fun styleEditTexts(){
        for (x in 0 until editTexts.size){
            var et = editTexts[x]
            if(x < focusIndex){
                styleFilled(et)
            }else if(x == focusIndex){
                styleHighlighted(et)
            }else if(x > focusIndex){
                styleDefault(et)
            }
        }
    }

    private fun styleDefault(editText: EditText) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeDefault.toFloat());
        editText.setTextColor(textColor)
        editText.background = backgroundImage
        editText.typeface = font
    }

    private fun styleHighlighted(editText: EditText) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, highlightedTextSize.toFloat());
        editText.setTextColor(highlightedTextColor)
        editText.background = highlightedBackgroundImage
        editText.typeface = highlightedFont
    }

    private fun styleFilled(editText: EditText) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, filledTextSize.toFloat());
        editText.setTextColor(filledTextColor)
        editText.background = filledBackgroundImage
        editText.typeface = filledFont
    }


    // Utility

    private val Int.dpTopx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun customBackground() : Drawable{
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 8.dpTopx.toFloat()
        shape.setColor(Color.WHITE)
        shape.setStroke(2.dpTopx, Color.BLACK)
        return shape
    }

    private fun showKeyboard(show:Boolean, editText: EditText){

        val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if(show){
            imm?.showSoftInput(editText, 0)
        }else {
            imm?.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
        }
    }

    // Public

    fun setOnFinishListener(func: (String) -> Unit) {
        onFinishFunction = func
    }

    fun setText(str:String){
        disableEditListener = true
        for(x in 0 until editTexts.size){
            if(x < str.length){
                editTexts[x].setText(str[x].toString())
            }else{
                editTexts[x].setText("")
            }
        }
        if(str.count() < editTexts.size){
            focusIndex = str.count()
            disableEditListener = false
            showKeyboard(true, editTexts[focusIndex])
        }else{
            editTexts.forEach {
                it.clearFocus()
            }
            focusIndex = editTexts.size
            disableEditListener = false
            showKeyboard(false, editTexts.last())
        }
        styleEditTexts()
    }

    fun clearText(showKeyboard: Boolean){
        disableEditListener = true
        for(x in 0 until editTexts.size){
            editTexts[x].setText("")
        }
        focusIndex = 0
        disableEditListener = false
        showKeyboard(showKeyboard, editTexts[focusIndex])
    }
}