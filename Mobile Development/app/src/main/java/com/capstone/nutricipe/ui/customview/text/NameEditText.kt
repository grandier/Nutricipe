package com.capstone.nutricipe.ui.customview.text

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.nutricipe.R

class NameEditText : AppCompatEditText, View.OnTouchListener {

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
        hint = resources.getString(R.string.hint_name)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        maxLines = 1
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateName(s)
            }

            override fun afterTextChanged(s: Editable?) {
                validateName(s)
            }
        })
    }

    private fun validateName(s: CharSequence?) {
        if (!s.isNullOrBlank() && s.length < 4) {
            error = resources.getString(R.string.min_name)
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_person_24_error, 0, 0, 0
            )
            setBackgroundResource(R.drawable.edt_bg_error)
        } else {
            error = null
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_person_24, 0, 0, 0
            )
            setBackgroundResource(R.drawable.edt_bg)
        }
    }


    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }
}