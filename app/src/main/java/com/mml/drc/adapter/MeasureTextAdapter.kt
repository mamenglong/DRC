package com.mml.drc.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.mml.drc.R
import com.mml.drc.utils.BaseListAdapter

/**
 * # MeasureTextAdapter
 *
 * @author 11324
 * 2019/1/25
 */
class MeasureTextAdapter(context: Context, dataSet: MutableList<Double>)
    : BaseListAdapter<TextViewHolder, Double>(context, dataSet) {
    override fun layoutId(position: Int): Int = R.layout.item_measure_edit_view
    override fun onCreateViewHolder(view: View): TextViewHolder = TextViewHolder(view)

    override fun onBindView(holder: TextViewHolder, pos: Int, item: Double) {
        holder.editView.tag = pos
        holder.editView.setText(item.toString())
        val lis = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setItem(holder.editView.tag as Int, try {
                    s.toString().toDouble()
                } catch (e: Throwable) {
                    0.0
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        holder.editView.addTextChangedListener(lis)

        holder.delBtn.setOnClickListener {
            holder.editView.removeTextChangedListener(lis)
            removeAt(pos)
            notifyDataSetChanged()
        }
    }
}

class TextViewHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    val editView = itemView.findViewById<EditText>(R.id.edit_view)
    val delBtn = itemView.findViewById<Button>(R.id.delete_btn)
}