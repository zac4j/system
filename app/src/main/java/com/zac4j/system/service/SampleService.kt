package com.zac4j.system.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.zac4j.system.ui.MainActivity

/**
 * Service code sample.
 *
 * @author: Zac
 * @date: 2022/3/16
 */
class SampleService : Service() {

  companion object {
    private const val NOTIFICATION_ID = 0x1001;
    private const val CHANNEL_NAME = "default";
  }

  private var binder: IBinder? = null

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
    startForeground(NOTIFICATION_ID, notification)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    // 组件调用 startService() 方法启动服务, 返回值表示 service 被 kill 后的行为
    return START_STICKY
  }

  override fun onUnbind(intent: Intent?): Boolean {
    // 所有绑定的客户端都已调用 unbindService() 方法解绑
    return super.onUnbind(intent)
  }

  override fun onRebind(intent: Intent?) {
    // 客户端在onUnbind() 方法回调之后，调用 bindService() 方法绑定服务
    super.onRebind(intent)
  }

  override fun onBind(intent: Intent?): IBinder? {
    // 客户端通过 bindService() 方法绑定服务
    return binder
  }
}