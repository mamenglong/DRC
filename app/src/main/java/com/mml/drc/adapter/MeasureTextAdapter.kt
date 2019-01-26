package com.mml.drc.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.mml.drc.R
import com.mml.drc.utils.BaseListAdapter

/**
 * # MeasureTextAdapter
 *
 * @author 11324
 * 2019/1/25
 */
class MeasureTextAdapter(context: Context, dataSet: MutableList<String?>)
    : BaseListAdapter<TextViewHolder, String?>(context, dataSet) {
    override fun layoutId(position: Int): Int = R.layout.item_measure_edit_view
    override fun onCreateViewHolder(view: View): TextViewHolder = TextViewHolder(view)

    override fun onBindView(holder: TextViewHolder, pos: Int, item: String?) {
        holder.editView.tag = pos
        holder.editView.setText(item)
        val lis = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setItem(holder.editView.tag as Int, try {
                    s.toString()
                } catch (e: Throwable) {
                    ""
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        holder.editView.addTextChangedListener(lis)
        holder.noView.text = "测量值${pos + 1}"
//        holder.delBtn.setOnClickListener {
        //            holder.editView.removeTextChangedListener(lis)
//            removeAt(pos)
//            notifyDataSetChanged()
//        }
    }

    fun disableAll() {
        val size = holders.size()
        for (i in 0 until size) {
            val h = holders[i]
            try {
                (h as TextViewHolder).editView.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}

class TextViewHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    val editView = itemView.findViewById<EditText>(R.id.edit_view)
    //    val delBtn = itemView.findViewById<Button>(R.id.delete_btn)
    val noView = itemView.findViewById<TextView>(R.id.no)

}