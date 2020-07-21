package com.zac4j.system.ui.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zac4j.system.ui.adapter.MessageAdapter.MessageViewHolder

/**
 * Adapter for WebSocket message list
 *
 * @author: zac
 * @date: 2020/7/21
 */
class MessageAdapter : Adapter<MessageViewHolder>() {

  private val messages: MutableList<String> = mutableListOf()

  fun addMessage(msg: String) {
    messages.add(msg)
    notifyItemInserted(messages.size)
    notifyItemRangeChanged(messages.size, 1)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MessageViewHolder {
    val itemView = TextView(parent.context)
    return MessageViewHolder(itemView)
  }

  override fun getItemCount() = messages.size

  override fun onBindViewHolder(
    holder: MessageViewHolder,
    position: Int
  ) {
    val msg = messages[position]
    val textView = holder.itemView as TextView
    val isServerMsg = position % 2 == 0
    textView.gravity = if (isServerMsg) Gravity.START else Gravity.END
    textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    textView.text = msg
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0F)
  }

  class MessageViewHolder(itemView: View) : ViewHolder(itemView)
}