package com.mml.drc.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.mml.drc.R
import com.mml.drc.utils.BaseListAdapter

/**
 * # GridAdapter
 *
 * @author 11324
 * 2019/1/25
 */
class GridAdapter(context: Context, dataSet: MutableList<String>)
    : BaseListAdapter<GridItemHolder, String>(context, dataSet) {

    override fun layoutId(position: Int): Int = R.layout.item_grid_image_view

    override fun onCreateViewHolder(view: View): GridItemHolder = GridItemHolder(view)

    override fun onBindView(holder: GridItemHolder, pos: Int, item: String) {
        holder.imageView.loadFile(item)
    }
}

class GridItemHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView){
    val imageView= itemView.findViewById<ImageView>(R.id.image_view)!!
}