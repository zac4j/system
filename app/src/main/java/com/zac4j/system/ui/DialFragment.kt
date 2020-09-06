package com.zac4j.system.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zac4j.system.R

/**
 * Desc:
 *
 * @author: zac
 * @date: 2020/9/5
 */
class DialFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_dial, container, false)
  }
}