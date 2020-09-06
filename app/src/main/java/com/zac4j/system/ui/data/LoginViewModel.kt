package com.zac4j.system.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zac4j.system.network.LoginRepository
import com.zac4j.system.network.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.Response
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

  @ExperimentalCoroutinesApi fun login(
    username: String,
    token: String
  ) {
    val json = "{username: \"$username\", token: \"$token\"}"
    // ↓未将 Dispatcher 传递给 launch，默认所有协程都会在主线程执行。
    viewModelScope.launch {
      // Make the network call and suspend execution until it finishes.
      val result = try {
        loginRepository.login(json)
      } catch (e: Exception) {
        Result.Error(Exception("Network request failed"))
      }

      when (result) {
        is Result.Success<Response> -> {
        } // do parse
        else -> {
        } // show error UI
      }
    }
  }
}