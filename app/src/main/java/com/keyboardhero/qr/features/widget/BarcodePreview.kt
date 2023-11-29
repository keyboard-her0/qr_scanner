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
import android.view.SurfaceView

class BarcodePreview @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defStyleAttr) {

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
            color = Color.RED
            isAntiAlias = true
        }
        valueZoom.duration = DURATION_ZOOM
        valueZoom.repeatCount = ValueAnimator.INFINITE
        valueZoom.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Float
            rectDetection = getRectZoomIn(rectDetection, animatedValue)
            invalidate()
        }
        status = Status.CHECKING
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val defaultSize = Math.min(h, w) * 2 / 3
        val left = (w - defaultSize) / 2F
        val top = (h - defaultSize) / 2F

        rectDetectionDefault = RectF(
            left, top, left + defaultSize, top + defaultSize
        )
        setDefault()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
            canvas?.drawRect(rectDetection, backgroundPain)
            if (status == Status.DETECTED || status == Status.OFF) {
                canvas?.drawRect(rectDetectionDefault, backgroundPain)
                valueZoom.cancel()
            } else if (!valueZoom.isStarted) {
                valueZoom.start()
            }
        }
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
        invalidate()
    }

    fun setDefault() {
        status = Status.CHECKING
        rectDetection = rectDetectionDefault
        invalidate()
    }

    fun startAnimation() {
        status = Status.CHECKING
        if (!valueZoom.isStarted) {
            valueZoom.start()
        }
    }

    fun stopAnimation() {
        status = Status.OFF
        valueZoom.cancel()
    }

    private fun getRectZoomIn(rectF: RectF, value: Float): RectF {
        rectF.left -= value
        rectF.top -= value
        rectF.right += value
        rectF.bottom += value
        return rectF
    }

    fun getStatus() = status

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