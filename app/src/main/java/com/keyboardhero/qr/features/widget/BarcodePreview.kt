package com.keyboardhero.qr.features.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.keyboardhero.qr.R

class BarcodePreview @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val borderPain = Paint()
    private val backgroundPain = Paint()

    private var bgColor = Color.parseColor("#4D000000")
    private var rectBorder: RectF = RectF()
    private var edgeLong = 250F
    private val pathLineScan = Path()
    private val valueAnimator = ValueAnimator.ofFloat(0F, 1F, 0F)

    private var outLinePath = Path()
    private val fullViewPath = Path()

    private var pathTopLeft = Path()
    private var pathTopRight = Path()
    private var pathBottomRight = Path()
    private var pathBottomLeft = Path()

    var isScanAnimation: Boolean = false
        set(value) {
            field = value
            if (value) {
                if (!valueAnimator.isStarted) {
                    valueAnimator.start()
                }
            } else {
                pathLineScan.reset()
                valueAnimator.cancel()
                invalidate()
            }
        }

    init {
        borderPain.apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = ContextCompat.getColor(context, R.color.green)
            isAntiAlias = true
        }

        backgroundPain.apply {
            style = Paint.Style.FILL
            color = bgColor
        }

        valueAnimator.apply {
            duration = DURATION_ANIMATION
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animatorValue ->
                val value = animatorValue.animatedValue as Float
                val valueY = rectBorder.top + PADDING_LINE_SCAN_VERTICAL +
                        ((rectBorder.height() - 2 * PADDING_LINE_SCAN_VERTICAL) * value)
                pathLineScan.reset()
                pathLineScan.moveTo(rectBorder.left + PADDING_LINE_SCAN_HORIZONTAL, valueY)
                pathLineScan.lineTo(rectBorder.right - PADDING_LINE_SCAN_HORIZONTAL, valueY)
                invalidate()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val defaultSize = h.coerceAtMost(w) * 2 / 3
        val left = (w - defaultSize) / 2F
        val top = (h - defaultSize) / 2F
        rectBorder = RectF(
            left, top, left + defaultSize, top + defaultSize
        )
        edgeLong = defaultSize / 4.5F
        fullViewPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(w.toFloat(), 0f)
            lineTo(w.toFloat(), h.toFloat())
            lineTo(0F, h.toFloat())
            close()
        }
        iniPath()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas, borderPain)
//        canvas.drawPath(outLinePath, borderPain)
//        canvas.drawPath(fullViewPath, backgroundPain)
        if (isScanAnimation) {
            drawLineScan(canvas, pathLineScan, borderPain)
        }
    }

    private fun drawLineScan(canvas: Canvas, path: Path, pain: Paint) {
        canvas.drawPath(path, pain)
    }

    private fun iniPath() {

        val fullBorderPath = Path()

        //Top Left
        val firstPointTopLeft = PointF(rectBorder.left, rectBorder.top + edgeLong)
        val secondPointTopLeft = PointF(rectBorder.left, rectBorder.top)
        val thirdPointTopLeft = PointF(rectBorder.left + edgeLong, rectBorder.top)
        pathTopLeft = getPathTopLeft(
            firstPointTopLeft, secondPointTopLeft, thirdPointTopLeft, edgeLong
        )
        fullBorderPath.moveTo(firstPointTopLeft.x, firstPointTopLeft.y)
        fullBorderPath.addPath(pathTopLeft)

        //Top Right
        val firstPointTopRight = PointF(rectBorder.right - edgeLong, rectBorder.top)
        val secondPointTopRight = PointF(rectBorder.right, rectBorder.top)
        val threePointTopRight = PointF(rectBorder.right, rectBorder.top + edgeLong)
        pathTopRight = getPathTopRight(
            firstPointTopRight, secondPointTopRight, threePointTopRight, edgeLong
        )
        fullBorderPath.lineTo(firstPointTopRight.x, firstPointTopRight.y)
        fullBorderPath.addPath(pathTopRight)

        //Bottom Right
        val firstPointBottomRight = PointF(rectBorder.right, rectBorder.bottom - edgeLong)
        val secondPointBottomRight = PointF(rectBorder.right, rectBorder.bottom)
        val threePointBottomRight = PointF(rectBorder.right - edgeLong, rectBorder.bottom)
        pathBottomRight = getPathBottomRight(
            firstPointBottomRight, secondPointBottomRight, threePointBottomRight, edgeLong
        )
        fullBorderPath.lineTo(firstPointBottomRight.x, firstPointBottomRight.y)
        fullBorderPath.addPath(pathBottomRight)

        //Bottom Left
        val firstPointBottomLeft = PointF(rectBorder.left + edgeLong, rectBorder.bottom)
        val secondPointBottomLeft = PointF(rectBorder.left, rectBorder.bottom)
        val threePointBottomLeft = PointF(rectBorder.left, rectBorder.bottom - edgeLong)
        pathBottomLeft = getPathBottomLeft(
            firstPointBottomLeft, secondPointBottomLeft, threePointBottomLeft, edgeLong
        )
        fullBorderPath.lineTo(firstPointBottomLeft.x, firstPointBottomLeft.y)
        fullBorderPath.addPath(pathBottomLeft)
        fullBorderPath.lineTo(firstPointTopLeft.x, firstPointTopLeft.y)

//        outLinePath.reset()
//        outLinePath = fullBorderPath

        outLinePath.op(fullBorderPath, fullViewPath, Path.Op.INTERSECT)
    }

    private fun getPathTopLeft(
        firstPointF: PointF,
        secondPoint: PointF,
        thirdPoint: PointF,
        edgeLong: Float
    ): Path {
        val pathTopLeft = Path()
        pathTopLeft.moveTo(firstPointF.x, firstPointF.y)
        pathTopLeft.lineTo(firstPointF.x, firstPointF.y - edgeLong / 2)
        pathTopLeft.quadTo(
            secondPoint.x,
            secondPoint.y,
            thirdPoint.x - edgeLong / 2,
            thirdPoint.y
        )
        pathTopLeft.lineTo(thirdPoint.x, thirdPoint.y)
        return pathTopLeft
    }

    private fun getPathTopRight(
        firstPointF: PointF,
        secondPoint: PointF,
        thirdPoint: PointF,
        edgeLong: Float
    ): Path {
        val pathTopRight = Path()
        pathTopRight.moveTo(firstPointF.x, firstPointF.y)
        pathTopRight.lineTo(firstPointF.x + edgeLong / 2, firstPointF.y)
        pathTopRight.quadTo(
            secondPoint.x,
            secondPoint.y,
            thirdPoint.x,
            thirdPoint.y - edgeLong / 2
        )
        pathTopRight.lineTo(thirdPoint.x, thirdPoint.y)
        return pathTopRight
    }

    private fun getPathBottomRight(
        firstPointF: PointF,
        secondPoint: PointF,
        thirdPoint: PointF,
        edgeLong: Float
    ): Path {
        val pathBottomRight = Path()
        pathBottomRight.moveTo(firstPointF.x, firstPointF.y)
        pathBottomRight.lineTo(firstPointF.x, firstPointF.y + edgeLong / 2)
        pathBottomRight.quadTo(
            secondPoint.x,
            secondPoint.y,
            thirdPoint.x + edgeLong / 2,
            thirdPoint.y
        )
        pathBottomRight.lineTo(thirdPoint.x, thirdPoint.y)
        return pathBottomRight
    }

    private fun getPathBottomLeft(
        firstPointF: PointF,
        secondPoint: PointF,
        thirdPoint: PointF,
        edgeLong: Float
    ): Path {
        val pathBottomLeft = Path()
        pathBottomLeft.moveTo(firstPointF.x, firstPointF.y)
        pathBottomLeft.lineTo(firstPointF.x - edgeLong / 2, firstPointF.y)
        pathBottomLeft.quadTo(
            secondPoint.x,
            secondPoint.y,
            thirdPoint.x,
            thirdPoint.y + edgeLong / 2
        )
        pathBottomLeft.lineTo(thirdPoint.x, thirdPoint.y)
        return pathBottomLeft
    }

    private fun drawBorder(canvas: Canvas, borderPain: Paint) {
        canvas.drawPath(pathTopLeft, borderPain)
        canvas.drawPath(pathTopRight, borderPain)
        canvas.drawPath(pathBottomRight, borderPain)
        canvas.drawPath(pathBottomLeft, borderPain)
    }

    companion object {
        private const val DURATION_ANIMATION = 3000L
        private const val PADDING_LINE_SCAN_VERTICAL = 30F
        private const val PADDING_LINE_SCAN_HORIZONTAL = 20F
    }
}