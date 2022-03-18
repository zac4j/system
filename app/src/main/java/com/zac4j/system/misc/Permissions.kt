package com.zac4j.system.misc

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

/**
 * Desc:Android Dynamic Permission Processor
 * (Take a note from great https://github.com/nazmulidris/places-api-poc/)
 *
 * @author: zac
 * @date: 2019-10-20
 */

fun hasPermission(
  context: Context,
  permission: String
) =
  ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED

fun requestPermission(
  activity: Activity,
  permission: String,
  responseId: Int
) {
  ActivityCompat.requestPermissions(activity, arrayOf(permission), responseId)
}

/**
 * Request permission tasks
 */
interface PermissionTask {
  fun getRequestPermission(): String
  fun onPermissionGranted()
  fun onPermissionDenied()
}

object PermissionHandler {

  // REQUEST_CODE for request permission
  private const val REQ_CODE: Int = 123

  // Holds one pending task that will be run if permission is granted.
  private var pendingTask: PermissionTask? = null

  fun execPermTask(
    activity: Activity,
    task: PermissionTask
  ) {
    if (hasPermission(activity, task.getRequestPermission())) {
      task.onPermissionGranted()
    } else {
      requestPermission(activity, task.getRequestPermission(), REQ_CODE)
      pendingTask = task
    }
  }

  fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    when (PermissionResult.convert(requestCode, permissions, grantResults)) {
      is PermissionResult.Granted -> {
        pendingTask?.onPermissionGranted()
      }
      is PermissionResult.Denied -> {
        pendingTask?.onPermissionDenied()
      }
      else -> {}
    }
  }

  sealed class PermissionResult {
    class Granted(id: Int) : PermissionResult()
    class Denied(id: Int) : PermissionResult()
    class Cancelled(id: Int) : PermissionResult()

    companion object {
      fun convert(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
      ): PermissionResult {
        return when {
          // If request is cancelled, the result arrays are empty
          grantResults.isEmpty() -> Cancelled(requestCode)
          // Permission was granted, run the grant task.
          grantResults.first() == PERMISSION_GRANTED -> Granted(requestCode)
          // Permission denied.
          else -> Denied(requestCode)
        }
      }
    }
  }
}

