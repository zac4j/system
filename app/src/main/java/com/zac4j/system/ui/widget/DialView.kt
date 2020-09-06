package com.zac4j.system.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.zac4j.system.R
import com.zac4j.system.ui.widget.FanSpeed.LOW
import com.zac4j.system.ui.widget.FanSpeed.OFF
import com.zac4j.system.ui.widget.FanSpeed.MEDIUM
import com.zac4j.system.ui.widget.FanSpeed.HIGH
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class FanSpeed(val label: Int) {
  OFF(R.string.fan_off),
  LOW(R.string.fan_low),
  MEDIUM(R.string.fan_medium),
  HIGH(R.string.fan_high);

  fun next() = when (this) {
    OFF -> LOW
    LOW -> MEDIUM
    MEDIUM -> HIGH
    HIGH -> OFF
  }
}

/**
 * Size for drawing the dial indicators and labels.
 */
private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

/**
 * Desc:
 *
 * @author: zac
 * @date: 2020/9/5
 */
class DialView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  /**
   * Radius of the circle.
   */
  private var radius = 0.0F

  /**
   * The active selection.
   */
  private var fanSpeed = OFF

  /**
   * Color of different fan speed.
   */
  private var fanSpeedLowColor = 0
  private var fanSpeedMediumColor = 0
  private var fanSpeedHighColor = 0

  /**
   * Position variable which will be used to draw label and indicator circle position.
   */
  private val pointPosition: PointF = PointF(0.0F, 0.0F)

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    textAlign = Paint.Align.CENTER
    textSize = 55.0F
    typeface = Typeface.create("", Typeface.BOLD)
  }

  init {
    isClickable = true

    context.withStyledAttributes(attrs, R.styleable.DialView) {
      fanSpeedLowColor = getColor(R.styleable.DialView_fanSpeedLowColor, 0)
      fanSpeedMediumColor = getColor(R.styleable.DialView_fanSpeedMediumColor, 0)
      fanSpeedHighColor = getColor(R.styleable.DialView_fanSpeedHighColor, 0)
    }
  }

  override fun performClick(): Boolean {
    if (super.performClick()) return true

    fanSpeed = fanSpeed.next()
    contentDescription = resources.getString(fanSpeed.label)

    invalidate()
    return true
  }

  override fun onSizeChanged(
    width: Int,
    height: Int,
    oldWidth: Int,
    oldHeight: Int
  ) {
    radius = (min(width, height) / 2.0 * 0.8).toFloat()
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    // Set dial background color to green if selection not off.
    paint.color = when (fanSpeed) {
      OFF -> Color.GRAY
      LOW -> fanSpeedLowColor
      MEDIUM -> fanSpeedMediumColor
      HIGH -> fanSpeedHighColor
    }

    // Draw the dial.
    canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

    // Draw the indicator circle
    val markerRadius = radius + RADIUS_OFFSET_INDICATOR
    pointPosition.computeCoordinateForSpeed(fanSpeed, markerRadius)
    paint.color = Color.BLACK
    canvas?.drawCircle(pointPosition.x, pointPosition.y, radius / 12, paint)

    // Draw the text labels.
    val labelRadius = radius + RADIUS_OFFSET_LABEL
    for (i in FanSpeed.values()) {
      pointPosition.computeCoordinateForSpeed(i, labelRadius)
      val label = resources.getString(i.label)
      canvas?.drawText(label, pointPosition.x, pointPosition.y, paint)
    }

  }

  private fun PointF.computeCoordinateForSpeed(
    position: FanSpeed,
    radius: Float
  ) {
    // Angles are in radians.
    val startAngle = Math.PI * (9 / 8.0)
    val angle = startAngle + position.ordinal * (Math.PI / 4)
    x = (radius * cos(angle)).toFloat() + width / 2
    y = (radius * sin(angle)).toFloat() + height / 2
  }
}