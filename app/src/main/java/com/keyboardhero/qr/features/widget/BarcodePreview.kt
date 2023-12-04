package com.keyboardhero.qr.features.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.core.content.ContextCompat
import com.keyboardhero.qr.R

class BarcodePreview @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val backgroundPain = Paint()
    private var bgColor = Color.parseColor("#4D000000")
    private lateinit var rectDetection: RectF
    private lateinit var rectDetectionDefault: RectF
    private var status = Status.OFF
    private val valueZoom = ValueAnimator.ofFloat(0f, ROOM_VALUE, 0F, -ROOM_VALUE)

    init {
        backgroundPain.apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = ContextCompat.getColor(context, R.color.orange)
            isAntiAlias = true
        }
        valueZoom.duration = DURATION_ZOOM
        valueZoom.repeatCount = ValueAnimator.INFINITE
        valueZoom.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Float
            rectDetection = getRectZoomIn(rectDetection, animatedValue)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val defaultSize = Math.min(h, w) * 2 / 3
        val left = (w - defaultSize) / 2F
        val top = (h - defaultSize) / 2F

        rectDetectionDefault = RectF(
            left, top, left + defaultSize, top + defaultSize
        )

        rectDetection = rectDetectionDefault
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(rectDetection, backgroundPain)
    }

    fun rectDetection(rectDetection: Rect, sizeDetection: Size) {
        val widthRatio = width / sizeDetection.height.toFloat()
        val heightRatio = height / sizeDetection.width.toFloat()

        this.rectDetection = RectF(
            (rectDetection.left * widthRatio),
            (rectDetection.top * heightRatio),
            (rectDetection.right * widthRatio),
            (rectDetection.bottom * heightRatio),
        )

        status = Status.DETECTED
        stopAnimation()
        invalidate()
    }

    fun start() {
        if (status != Status.CHECKING) {
            status = Status.CHECKING
            rectDetection = rectDetectionDefault
            startAnimation()
        }
    }

    fun stop(isReset : Boolean = true) {
        status = Status.OFF
        if (isReset){
            stopAnimation()
            rectDetection = rectDetectionDefault
            invalidate()
        }
    }

    private fun startAnimation() {
        if (!valueZoom.isStarted) {
            valueZoom.start()
        }
    }

    private fun stopAnimation() {
        if (valueZoom.isStarted) {
            valueZoom.cancel()
        }
    }

    private fun getRectZoomIn(rectF: RectF, value: Float): RectF {
        rectF.left -= value
        rectF.top -= value
        rectF.right += value
        rectF.bottom += value
        return rectF
    }

    enum class Status {
        CHECKING,
        DETECTED,
        OFF,
    }

    companion object {
        private const val ROOM_VALUE = 2F
        private const val DURATION_ZOOM = 1000L
    }
}