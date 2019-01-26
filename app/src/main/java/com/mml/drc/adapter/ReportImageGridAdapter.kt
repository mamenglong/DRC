package com.mml.drc.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mml.drc.R
import com.mml.drc.utils.BaseListAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

/**
 * # ReportImageGridAdapter
 *
 * @author 11324
 * 2019/1/25
 */
class ReportImageGridAdapter(context: Context, dataSet: MutableList<String?>)
    : BaseListAdapter<GridItemHolder, String?>(context, dataSet) {

    override fun layoutId(position: Int): Int = R.layout.item_grid_image_view

    override fun onCreateViewHolder(view: View): GridItemHolder = GridItemHolder(view)

    override fun onBindView(holder: GridItemHolder, pos: Int, item: String?) {
        item?.also {
            holder.imageView.loadFile(item)
        }
        holder.btn.setOnClickListener {
            Matisse.from(context as Activity)
                    .choose(MimeType.ofImage())//图片类型
                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(1)//可选的最大数
                    .capture(true)//选择照片时，是否显示拍照
                    .captureStrategy(CaptureStrategy(true, "${context.packageName}.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(GlideEngine())//图片加载引擎
                    .forResult(1000 + pos)//
        }
        holder.imageView.setOnLongClickListener {
            removeAt(pos)
            dataSet.add(pos, null)
            notifyDataSetChanged()
            true
        }
        holder.noView.text = "测量图${pos + 1}"

    }

    fun disableAll() {
        val size = holders.size()
        for (i in 0 until size) {
            val h = holders[i]
            try {
                (h as GridItemHolder).imageView.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class GridItemHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    val imageView = itemView.findViewById<ImageView>(R.id.image_view)!!
    val btn = itemView.findViewById<Button>(R.id.sel_photo)
    val noView = itemView.findViewById<TextView>(R.id.no)

}