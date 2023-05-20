package com.capstone.nutricipe.ui.customview.text

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.nutricipe.R

class EmailEditText : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    private fun isValid(s: CharSequence): Boolean {
        return emailRegex.matches(s)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "email@email.com"
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !emailRegex.matches(s.toString())) {
                    error = resources.getString(R.string.invalid_email)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_email_24_error, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg_error)
                } else {
                    error = null
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_email_24, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.let { isValid(it) } == false && s.isNotEmpty()) {
                    error = resources.getString(R.string.invalid_email)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_email_24_error, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg_error)
                } else {
                    error = null
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_email_24, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg)
                }
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }


}