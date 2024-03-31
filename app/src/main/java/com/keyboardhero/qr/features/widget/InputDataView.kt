package com.keyboardhero.qr.features.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.ViewInputDataBinding

class InputDataView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewInputDataBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var icon: Int = 0
        set(value) {
            field = value
            binding.imgIcon.setImageResource(value)
        }

    var text: String = ""
        set(value) {
            field = value
            binding.editText.setText(value)
        }
        get() = binding.editText.text.toString()

    val editText = binding.editText

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.InputDataView, 0, 0
        ).apply {
            try {
                icon = getResourceId(R.styleable.InputDataView_startIcon, icon)
                editText.inputType =
                    getInt(R.styleable.InputDataView_android_inputType, InputType.TYPE_CLASS_TEXT)
                editText.maxLines =
                    getInt(R.styleable.InputDataView_android_maxLines, editText.maxLines)
            } finally {
                recycle()
            }
        }
    }


}