package com.zac4j.system.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zac4j.system.R
import com.zac4j.system.ui.adapter.MessageAdapter
import com.zac4j.system.util.Logger
import com.zac4j.system.util.Utils.mockWebServer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockWebServer
import okio.ByteString
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Desc:Network connection sample
 *
 * @author: zac
 * @date: 2020/7/21
 */
class NetworkFragment : Fragment() {

  companion object {
    private const val TAG = "NetworkFragment"

    /**
     * Sent ping but didn't receive pong within 2000ms (after 0 successful ping/pongs)
     */
    const val PING_INTERVAL = 2L
  }

  /**
   * Executor for WebSocket connection.
   */
  private val mExecutor: ExecutorService by lazy {
    Executors.newSingleThreadExecutor()
  }

  /**
   * Mock web server.
   */
  private val mServer: MockWebServer? by lazy {
    mockWebServer()
  }

  private val mAdapter: MessageAdapter by lazy {
    MessageAdapter()
  }

  private lateinit var mMessageListView: RecyclerView
  private lateinit var mHandler: Handler

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_network, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    mMessageListView = view.findViewById(R.id.network_msg_list)
    mMessageListView.apply {
      adapter = mAdapter
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    mHandler = Handler()

    val runnable = Runnable {
      mServer?.let {
        val hostname = it.hostName
        val port = it.port

        connectWebSocket(hostname, port)
      }
    }
    mExecutor.execute(runnable)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mHandler.removeCallbacksAndMessages(null)
  }

  private fun connectWebSocket(
    hostname: String,
    port: Int
  ) {
    val httpClient = OkHttpClient.Builder()
        .pingInterval(PING_INTERVAL, TimeUnit.SECONDS)
        .build()

    val url = "ws://${hostname}:${port}"
    Logger.i(TAG, "connectWebSocket", "connect url -> $url")

    val request = Request.Builder()
        .url(url)
        .build()

    httpClient.newWebSocket(request, object : WebSocketListener() {
      override fun onOpen(
        webSocket: WebSocket,
        response: Response
      ) {
        super.onOpen(webSocket, response)
        Logger.i(TAG, "onOpen", "response -> ${response.message}")
        // While web socket is opened create our interval work
        createIntervalWork(webSocket)
      }

      override fun onMessage(
        webSocket: WebSocket,
        text: String
      ) {
        super.onMessage(webSocket, text)
        Logger.i(TAG, "onMessage", "text -> $text")
        if (text.isNotEmpty()) {
          mHandler.post {
            mAdapter.addMessage("pong -> $text")
          }
        }
      }

      override fun onMessage(
        webSocket: WebSocket,
        bytes: ByteString
      ) {
        super.onMessage(webSocket, bytes)
        Logger.i(TAG, "onMessage", "bytes -> $bytes")
        mHandler.post {
          mAdapter.addMessage(bytes.toString())
        }
      }

      override fun onClosed(
        webSocket: WebSocket,
        code: Int,
        reason: String
      ) {
        super.onClosed(webSocket, code, reason)
        Logger.i(TAG, "onClosed", "reason -> $reason")
      }

      override fun onClosing(
        webSocket: WebSocket,
        code: Int,
        reason: String
      ) {
        super.onClosing(webSocket, code, reason)
        Logger.i(TAG, "onClosing", "reason -> $reason")
      }

      override fun onFailure(
        webSocket: WebSocket,
        t: Throwable,
        response: Response?
      ) {
        super.onFailure(webSocket, t, response)
        Logger.i(TAG, "onFailure", "${t.message}")
      }
    })
  }

  /**
   * Create interval work send message.
   */
  fun createIntervalWork(webSocket: WebSocket) {
    var tick = 0
    val runnable = object : Runnable {
      override fun run() {
        ++tick
        // Send message every 2s
        webSocket.send("$tick")
        mAdapter.addMessage("ping -> $tick")
        // Repeat this the same runnable code block again another 2 seconds
        mHandler.postDelayed(this, 2000L)
      }
    }
    mHandler.post(runnable)
  }

}