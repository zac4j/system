package com.zac4j.system.util

import android.os.Handler
import java.lang.reflect.Field

/**
 * Reflect utility methods
 *
 * @author: zac
 * @date: 2019-06-16
 */
class ReflectUtils {
  private fun createIntervalWork() {
    // Create the Handler object (on the main thread by default)
    val handler = Handler()
    // Define the code block to be executed
    val runnableCode: Runnable = object : Runnable {
      override fun run() {
        // Repeat this the same runnable code block again another 2 seconds
        // 'this' is referencing the Runnable object
        handler.postDelayed(this, 2000)
      }
    }
    // Start the initial runnable task by posting through the handler
    handler.post(runnableCode)
  }

  companion object {
    @JvmStatic fun getFieldValue(
      fieldName: String,
      target: Any
    ): Any {
      return try {
        getFieldValueUnchecked(fieldName, target)
      } catch (e: Exception) {
        throw IllegalStateException(e)
      }
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getFieldValueUnchecked(
      fieldName: String,
      target: Any
    ): Any {
      val field =
        findField(fieldName, target.javaClass)
      field.isAccessible = true
      return field[target]
    }

    @Throws(NoSuchFieldException::class) fun findField(
      name: String,
      clazz: Class<*>
    ): Field {
      var currentClass: Class<*>? = clazz
      while (currentClass != Any::class.java) {
        for (field in currentClass!!.declaredFields) {
          if (name == field.name) {
            return field
          }
        }
        currentClass = currentClass.superclass
      }
      throw NoSuchFieldException("Field $name not found for class $clazz")
    }
  }
}