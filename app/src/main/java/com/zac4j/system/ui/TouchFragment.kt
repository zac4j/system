package com.zac4j.system.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.zac4j.system.R
import com.zac4j.system.data.TouchViewModel
import com.zac4j.system.databinding.FragmentTouchBinding
import com.zac4j.system.util.Logger

/**
 * Desc:Android 事件分发示例
 *
 * @author: zac
 * @date: 2020/7/12
 */
class TouchFragment : Fragment() {

  companion object {
    private const val TAG = "TouchFragment"
  }

  private val viewModel by lazy {
    activity?.let {
      AndroidViewModelFactory.getInstance(it.application)
          .create(TouchViewModel::class.java)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding: FragmentTouchBinding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_touch, container, false)

    binding.viewModel = viewModel

    binding.lifecycleOwner = this
    binding.touchSampleBtn.setOnClickListener {
      Logger.vi(
          TAG, "touchSampleBtn::${binding.touchSampleBtn.javaClass.simpleName}",
          "setOnClickListener", "clicked"
      )
    }

    binding.touchSampleBtn.setOnTouchListener { _, ev ->
      Logger.vi(
          TAG, "touchSampleBtn::${binding.touchSampleBtn.javaClass.simpleName}",
          "setOnTouchListener",
          "MotionEvent::${MotionEvent.actionToString(ev.action)}"
      )
      false
    }

    binding.touchSampleContainer.requestDisallowInterceptTouchEvent(true)

    binding.touchSampleContainer.setOnTouchListener { _, event ->
      Logger.vi(
          TAG, "touchSampleContainer::${binding.touchSampleContainer.javaClass.simpleName}",
          "setOnTouchListener",
          "MotionEvent::${MotionEvent.actionToString(event.action)}"
      )
      false
    }

    return binding.root
  }

}