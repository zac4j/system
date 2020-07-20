package com.zac4j.system.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.zac4j.system.util.Logger

/**
 * Desc:Custom layout for testing ViewGroup touch event delivery.
 *
 * @author: zac
 * @date: 2020/7/13
 */
class CustomLayout : LinearLayout {

  constructor(context: Context) : super(context)

  constructor(
    context: Context,
    attrs: AttributeSet?
  ) : super(context, attrs)

  constructor(
    context: Context,
    attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    Logger.vi(
        "TouchFragment", this.javaClass.simpleName, "dispatchTouchEvent",
        "MotionEvent::${MotionEvent.actionToString(ev.action)}"
    )
    return super.dispatchTouchEvent(ev)
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    Logger.vi(
        "TouchFragment", this.javaClass.simpleName, "onInterceptTouchEvent",
        "MotionEvent::${MotionEvent.actionToString(ev.action)}"
    )
    return false
  }

  override fun onLayout(
    changed: Boolean,
    l: Int,
    t: Int,
    r: Int,
    b: Int
  ) {
    super.onLayout(changed, l, t, r, b)
  }
}