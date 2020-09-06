package com.zac4j.system.util

import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.ByteString
import java.io.Closeable
import java.io.IOException
import kotlin.coroutines.resume

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

  /**
   * Mock 网络服务器
   */
  fun mockWebServer(): MockWebServer? {
    val mockWebServer: MockWebServer? = MockWebServer()

    mockWebServer?.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {
      override fun onOpen(
        webSocket: WebSocket,
        response: Response
      ) {
        super.onOpen(webSocket, response)
        Logger.i("MockWebServer", "onOpen", "response -> ${response.message}")
      }

      override fun onMessage(
        webSocket: WebSocket,
        text: String
      ) {
        super.onMessage(webSocket, text)
        Logger.i("MockWebServer", "onMessage", "text -> $text")
        // send back message to client
        webSocket.send(text)
      }

      override fun onMessage(
        webSocket: WebSocket,
        bytes: ByteString
      ) {
        super.onMessage(webSocket, bytes)
        Logger.i("MockWebServer", "onMessage", "bytes -> $bytes")
        // send back message to client
        webSocket.send(bytes)
      }

      override fun onClosed(
        webSocket: WebSocket,
        code: Int,
        reason: String
      ) {
        super.onClosed(webSocket, code, reason)
        Logger.i("MockWebServer", "onClosed", "reason -> $reason")
      }

      override fun onClosing(
        webSocket: WebSocket,
        code: Int,
        reason: String
      ) {
        super.onClosing(webSocket, code, reason)
        Logger.i("MockWebServer", "onClosing", "reason -> $reason")
      }

      override fun onFailure(
        webSocket: WebSocket,
        t: Throwable,
        response: Response?
      ) {
        super.onFailure(webSocket, t, response)
        Logger.i("MockWebServer", "onFailure", "exception -> ${t.message}")
      }
    }))
    return mockWebServer
  }

  /**
   * Close quietly
   */
  fun Closeable?.closeQuietly() {
    if (this != null) {
      try {
        this.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { continuation ->

    val listener = object : View.OnLayoutChangeListener {
      override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
      ) {
        // remove listener after on layout change
        v?.removeOnLayoutChangeListener(this)
        // wake up coroutine and resume execution
        continuation.resume(Unit)
      }
    }

    // remove listener on cancel coroutine
    continuation.invokeOnCancellation { removeOnLayoutChangeListener(listener) }

    // add listener to view
    addOnLayoutChangeListener(listener)

  }

}