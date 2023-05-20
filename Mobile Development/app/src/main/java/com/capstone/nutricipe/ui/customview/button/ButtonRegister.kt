package com.capstone.nutricipe.ui.customview.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.capstone.nutricipe.R

class ButtonRegister @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        initView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) {
            ContextCompat.getDrawable(context, R.drawable.button_bg)
        } else {
            ContextCompat.getDrawable(context, R.drawable.button_bg_disable)
        }
        setTextColor(ContextCompat.getColor(context, android.R.color.background_light))
        textSize = 18f
        typeface = Typeface.DEFAULT_BOLD
        gravity = Gravity.CENTER
        text = context.getString(R.string.register)
    }

    private fun initView() {
        isEnabled = false
    }
}