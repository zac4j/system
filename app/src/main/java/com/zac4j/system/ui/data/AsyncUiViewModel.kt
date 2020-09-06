package com.zac4j.system.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AsyncUiViewModel : ViewModel() {

  fun foo() {
    viewModelScope.launch {

    }
  }

}


