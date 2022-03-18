package com.zac4j.system.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import androidx.annotation.RequiresApi
import com.zac4j.system.ui.MainActivity

/**
 * Service code sample.
 *
 * @author: Zac
 * @date: 2022/3/16
 */
class SampleIntentService : Service() {

  companion object {
    private const val NOTIFICATION_ID = 0x1001;
    private const val CHANNEL_NAME = "default";
  }

  private var serviceLooper: Looper? = null
  private var serviceHandler: ServiceHandler? = null

  private inner class ServiceHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
      // todo some work here

      // Stop the service using the startId, so that we don't stop the
      // service in the middle of handling another job
      stopSelf(msg.arg1)
    }
  }

  private val pendingIntent: PendingIntent =
    Intent(this, MainActivity::class.java).let { notificationIntent ->
      PendingIntent.getActivity(this, 0, notificationIntent, 0)
    }

  /**
   * Create foreground notification
   */
  @RequiresApi(VERSION_CODES.O)
  private val notification: Notification = Notification.Builder(this, CHANNEL_NAME)
    .setContentTitle("notification title")
    .setContentText("notification message")
    .setContentIntent(pendingIntent)
    .setTicker("Ticker")
    .build()

  override fun onCreate() {
    // Start up the thread running the service.
    // We create a separate thread because the service normally runs in
    // the process's main thread, which we don't want block. We also make
    // it background priority so CPU-intensive work will not disrupt the UI.
    HandlerThread("SampleIntentService", THREAD_PRIORITY_BACKGROUND).apply {
      start()

      // Get the HandlerThread's Looper
      serviceLooper = looper
      serviceHandler = ServiceHandler(looper)
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    startForeground(NOTIFICATION_ID, notification)

    // For each start request, send a message to start a job and deliver the
    // start ID so we know which request we're stopping when we finish the job
    serviceHandler?.obtainMessage()?.also { msg ->
      msg.arg1 = startId
      serviceHandler?.sendMessage(msg)
    }

    // If service get killed, after returning from here, restart
    return START_STICKY
  }

  override fun onBind(intent: Intent?): IBinder? {
    // No binder provider
    return null
  }
}