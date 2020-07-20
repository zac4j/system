package com.zac4j.system.util

import android.util.Log

/**
 * Desc:ZacLogger
 *
 * @author: zac
 * @date: 2020/7/13
 */
class Logger {

  companion object {

    private const val TAG = "ZacLog"

    /**
     * Common log info
     */
    fun i(
      page: String?,
      method: String?,
      msg: String?
    ) {
      if (page.isNullOrEmpty() or msg.isNullOrEmpty()) {
        Log.e(TAG, "Page or Message log must not empty!")
      } else {
        Log.i(TAG, "Page -> $page, ${method?.let { "Method -> $method" }}, Msg -> $msg")
      }
    }

    /**
     * Log info for view
     */
    fun vi(
      page: String?,
      view: String?,
      method: String?,
      msg: String?
    ) {
      Log.i(
          TAG,
          "Page -> $page, View -> $view, ${method?.let { "Method -> $method" }}, Msg -> $msg"
      )
    }

  }

}