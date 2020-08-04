package com.zac4j.system.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zac4j.system.network.LoginRepository
import com.zac4j.system.ui.data.LoginViewModel

class LoginViewModelFactory(
  val app: Application,
  private val loginRepository: LoginRepository
) : ViewModelProvider.AndroidViewModelFactory(app) {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return LoginViewModel(loginRepository) as T
  }
}