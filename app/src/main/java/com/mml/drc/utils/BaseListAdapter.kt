package com.mml.drc.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * # EasyListAdapter : BaseAdapter
 *
 * Created by Vove on 2018/6/14
 */

abstract class BaseListAdapter<VH : BaseListAdapter.ViewHolder, DataType>(
        internal var context: Context, private val dataSet: MutableList<DataType>) : BaseAdapter() {
    var inflater: LayoutInflater = LayoutInflater.from(context)

    protected abstract fun onCreateViewHolder(view: View): VH
    override fun getCount(): Int = dataSet.size
    /**
     * @return your layout
     */
    protected open fun layoutId(position: Int): Int = -1

    //必须实现其一layoutId|layoutView
    protected open fun layoutView(position: Int): View? = null

    /**
     * Init your item contentView with a holder
     */
    protected abstract fun onBindView(holder: VH, pos: Int, item: DataType)

    override fun getItem(position: Int): DataType = dataSet[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: VH
        if (view == null) {
            view = layoutView(position) ?: inflater.inflate(layoutId(position), null)!!
            holder = onCreateViewHolder(view)
            view.tag = holder
        } else holder = view.tag as VH

        onBindView(holder, position, getItem(position))
        return view
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun removeAt(pos: Int) {
        dataSet.removeAt(pos)
        notifyDataSetChanged()
    }

    protected fun setItem(p: Int, d: DataType) {
        dataSet[p] = d
    }

    abstract class ViewHolder(val itemView: View)

}
