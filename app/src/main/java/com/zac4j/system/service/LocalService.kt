package com.zac4j.system.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 *
 *
 * @author: zac
 * @date: 2022/3/17
 */
class LocalService : Service(){

  private val binder = LocalBinder()

  override fun onBind(intent: Intent?): IBinder? {
    return binder
  }

  inner class LocalBinder: Binder() {
    fun getService() : LocalService = this@LocalService
  }
}