package com.zac4j.system.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.zac4j.system.R
import com.zac4j.system.R.layout
import com.zac4j.system.data.MainViewModel
import com.zac4j.system.databinding.ActivityMainBinding
import com.zac4j.system.util.screenshot.ScreenshotHelper

class MainActivity : AppCompatActivity() {

  companion object {
    private const val TAG = "MainActivity"
  }

  private val viewModel by lazy {
    AndroidViewModelFactory.getInstance(application)
        .create(MainViewModel::class.java)
  }

  private val mImageView: ImageView? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, layout.activity_main)

    binding.viewModel = viewModel

    binding.lifecycleOwner = this

    setSupportActionBar(binding.toolbar)

    val networkPage = NetworkFragment()
    val touchPage = TouchFragment()
    val dialPage = DialFragment()
    supportFragmentManager.beginTransaction()
        .add(R.id.fragment_container, dialPage)
        .addToBackStack(null)
        .commit()

    binding.fab.setOnClickListener { view ->
      showSnackBar(view)
    }
  }

  private fun showSnackBar(view: View) {
    Snackbar.make(view, "Hi", Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show()
  }

  private fun showDialog() {
    val alertDialog =
      Builder(this)
          .setMessage("!")
          .setPositiveButton("OK", null)
          .setNegativeButton("Cancel", null)
          .create()
    alertDialog.setOnShowListener {
      val okBtn =
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
      okBtn.setOnClickListener {
        val bitmap = ScreenshotHelper.takeScreenshot(this@MainActivity)
        mImageView!!.setImageBitmap(bitmap)
      }
    }
    alertDialog.show()
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
//    Logger.i(
//        TAG, "onTouchEvent",
//        "MotionEvent::${MotionEvent.actionToString(ev.action)}"
//    )
    return super.onTouchEvent(ev)
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//    Logger.i(
//        TAG, "dispatchTouchEvent",
//        "MotionEvent::${MotionEvent.actionToString(ev.action)}"
//    )
    return super.dispatchTouchEvent(ev)
  }
}