package com.mml.drc.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mml.drc.Model.ReportItem
import com.mml.drc.R
import com.mml.drc.utils.BaseListAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.io.File


/**
 * # NewReportListAdapter
 * 新建报告列表适配器
 * @author 11324
 * 2019/1/25
 */
class NewReportListAdapter(context: Context, dataSet: List<ReportItem>?)
    : BaseListAdapter<NewReportItemHolder, ReportItem>(context, dataSet) {

    override fun onCreateViewHolder(view: View): NewReportItemHolder = NewReportItemHolder(view)

    override fun layoutId(position: Int): Int = R.layout.item_new_report

    override fun onBindView(holder: NewReportItemHolder, pos: Int, item: ReportItem) {
        item.imagePath?.apply {
            //加载图片
            holder.imageView.loadFile(this)
        }
        item.measurementValue?.apply {
            holder.measureView.setText(this.toString())
        }
        holder.measureView.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.measurementValue = try { s?.toString()?.toDouble() } catch (e:Throwable){ null }
                Log.d("Debug :", "afterTextChanged  ----> ${item.measurementValue}")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        holder.imageView.setOnClickListener {
            //选择图片
//            FilePickerBuilder.instance.setMaxCount(1)
//                    .setActivityTheme(R.style.LibAppTheme)
//                    .pickPhoto(context as Activity, 1000 + pos)//选择图片requestCode > 1000
            Matisse.from(context as Activity)
                    .choose(MimeType.ofImage())//图片类型
                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(1)//可选的最大数
                    .capture(true)//选择照片时，是否显示拍照
                    .captureStrategy(CaptureStrategy(true, context.packageName + ".fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(GlideEngine())//图片加载引擎
                    .forResult(1000 + pos)//

        }
    }
}

fun ImageView.loadFile(filePath: String) {
    Glide.with(context)
            .load(File(filePath))
            .placeholder(R.drawable.ic_plus)
            .error(R.drawable.ic_error_black_24dp)
            .into(this)

}

class NewReportItemHolder(itemView: View) : BaseListAdapter.ViewHolder(itemView) {
    val imageView = itemView.findViewById<ImageView>(R.id.image_view)
    val measureView = itemView.findViewById<EditText>(R.id.measure_value_view)
}