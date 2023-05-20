package com.capstone.nutricipe.ui.customview.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.capstone.nutricipe.R

class ButtonLogin : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground

        setTextColor(txtColor)
        textSize = 18f
        typeface = Typeface.DEFAULT_BOLD
        gravity = Gravity.CENTER
        text = if (isEnabled) "Login" else "Login"
    }

    private fun initView() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.button_bg) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.button_bg_disable) as Drawable
    }
}