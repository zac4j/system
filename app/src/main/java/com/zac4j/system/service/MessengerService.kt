package com.zac4j.system.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Toast

/** Command to the service to display a message  */
private const val MSG_SAY_HELLO = 1

class MessengerService : Service() {

  /**
   * Target we publish for clients to send messages to IncomingHandler.
   */
  private lateinit var mMessenger: Messenger

  /**
   * Handler of incoming messages from clients.
   */
  internal class IncomingHandler(
    context: Context,
    looper: Looper,
    private val applicationContext: Context = context.applicationContext
  ) : Handler(looper) {
    override fun handleMessage(msg: Message) {
      when (msg.what) {
        MSG_SAY_HELLO ->
          Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show()
        else -> super.handleMessage(msg)
      }
    }
  }

  /**
   * When binding to the service, we return an interface to our messenger
   * for sending messages to the service.
   */
  override fun onBind(intent: Intent): IBinder? {
    Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
    mMessenger = Messenger(IncomingHandler(this, Looper.getMainLooper()))
    return mMessenger.binder
  }
}