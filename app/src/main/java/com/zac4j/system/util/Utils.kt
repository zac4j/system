package com.zac4j.system.util

import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.util.DisplayMetrics
import java.io.Closeable
import java.io.IOException

/**
 * Common useful methods.
 *
 * @author: zac
 * @date: 2019-06-16
 */
object Utils {
  @JvmStatic fun getScreenSize(
    activity: Activity,
    size: IntArray
  ) {
    val metrics = DisplayMetrics()
    activity.windowManager
        .defaultDisplay
        .getMetrics(metrics)
    size[0] = metrics.widthPixels
    size[1] = metrics.heightPixels
  }

  @JvmStatic fun isEmpty(c: Collection<*>?) = c == null || c.isEmpty()

  /**
   * 粘贴剪贴板内容
   */
  fun pastePlainText(ctx: Context): String {
    val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    var pasteData = ""
    if (isPasteEnable(clipboard)) {
      clipboard?.also {
        val clipItem = it.primaryClip?.getItemAt(0)
        clipItem?.let { item ->
          pasteData = item.text.toString()
        }
      }
    }
    return pasteData
  }

  /**
   * 是否可以粘贴剪贴板内容
   */
  private fun isPasteEnable(clipboard: ClipboardManager?): Boolean {
    return clipboard?.let {
      return it.hasPrimaryClip() && it.primaryClipDescription?.hasMimeType(
          ClipDescription.MIMETYPE_TEXT_PLAIN
      ) == true
    } ?: false
  }

  fun closeQuietly(c: Closeable?) {
    if (c != null) {
      try {
        c.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
}