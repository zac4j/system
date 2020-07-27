package com.zac4j.system.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.zac4j.system.R
import com.zac4j.system.databinding.FragmentLoginBinding
import com.zac4j.system.ui.data.LoginViewModel

class LoginFragment : Fragment() {
  companion object {
    private const val TAG = "LoginFragment"
  }

  private val viewModel by lazy {
    activity?.let {
      AndroidViewModelFactory.getInstance(it.application)
          .create(LoginViewModel::class.java)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding: FragmentLoginBinding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

    binding.viewModel = viewModel

    binding.lifecycleOwner = this

    return binding.root
  }
}