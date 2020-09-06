package com.zac4j.system.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewModelScope
import com.zac4j.system.R
import com.zac4j.system.databinding.FragmentAsyncUiBinding
import com.zac4j.system.ui.data.AsyncUiViewModel
import com.zac4j.system.util.Utils.awaitNextLayout
import kotlinx.android.synthetic.main.fragment_async_ui.async_ui_tv
import kotlinx.coroutines.launch

class AsyncUiFragment : Fragment() {

  companion object {
    private const val TAG = "AsyncUiFragment"
  }

  private val viewModel by lazy {
    activity?.let {
      AndroidViewModelFactory.getInstance(it.application)
          .create(AsyncUiViewModel::class.java)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding: FragmentAsyncUiBinding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_async_ui, container, false)

    binding.viewModel = viewModel

    binding.lifecycleOwner = this

    return binding.root
  }

  override fun onResume() {
    super.onResume()
    makeMagicText()
  }

  private fun makeMagicText() {
    viewModel?.viewModelScope?.launch {
      // set textView invisible first and set some text
      async_ui_tv.isInvisible = true
      async_ui_tv.text = "你们好!"

      // waiting for next layout task, then get its height
      async_ui_tv.awaitNextLayout()

      // layout task executed, now set textView is visible,and translate top
      async_ui_tv.isVisible = true
      async_ui_tv.translationY = -async_ui_tv.height.toFloat()
      async_ui_tv.animate()
          .translationY(0f)
    }
  }

}