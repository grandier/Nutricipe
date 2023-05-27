package com.capstone.nutricipe.ui.customview.text

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.nutricipe.R
import java.util.regex.Pattern

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "******"
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private val pattern: Pattern = Pattern.compile(".{8,}")

    private fun isValid(s: CharSequence): Boolean {
        return pattern.matcher(s).matches()
    }

    private fun init() {
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) {
                    error = resources.getString(R.string.password_min_length)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_lock_24_error, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg_error)
                } else {
                    error = null
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_lock_24, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.let { isValid(it) } == false && s.isNotEmpty()) {
                    error = resources.getString(R.string.password_min_length)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_lock_24_error, 0, 0, 0
                    )
                    setBackgroundResource(R.drawable.edt_bg_error)
                } else {
                    error = null
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.baseline_lock_24, 0, 0, 0
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