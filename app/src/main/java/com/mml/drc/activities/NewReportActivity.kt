package com.mml.drc.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mml.drc.Model.Report
import com.mml.drc.R
import com.mml.drc.adapter.GridAdapter
import com.mml.drc.adapter.MeasureTextAdapter
import com.mml.drc.utils.toastLong
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_new_report.*
import org.litepal.LitePal
import java.util.*

class NewReportActivity : AppCompatActivity() {

    private lateinit var report: Report//TODO 操作员
    private val gridAdapter by lazy { GridAdapter(this, report.photoPaths) }
    private val measureAdapter by lazy { MeasureTextAdapter(this, report.measurementValue) }


    companion object {
        const val ERR_ID = -111L
        const val REQUEST_CODE_CHOOSE_PHOYO = 1000
    }

    var id: Long = ERR_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_report)

        id = intent?.getLongExtra("id", ERR_ID) ?: ERR_ID
        try {
            report = if (id != ERR_ID) {//编辑
                LitePal.find(Report::class.java, id)
            } else Report()
        } catch (e: Throwable) {//新建
            e.printStackTrace()
            toastLong("程序错误")
            finish()
        }

        requestPermission()
        measure_list_view.adapter = measureAdapter
        report_photo_grid.adapter = gridAdapter
        report_photo_grid.numColumns = 3

        val s = LitePal.findAll(Report::class.java)
        Log.d("Debug :", "onCreate 报告数量 ----> ${s.size}")
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
        ), 9)
    }

    /**
     * 保存到本地
     * 保存或更新
     */
    private fun saveOrUpdate() {
        if (id == ERR_ID) {
            Log.d("Debug :", "saveOrUpdate  ----> 新建")
            report.save()
        } else {
            Log.d("Debug :", "saveOrUpdate  ----> 更新")
            report.update(id)//更新
        }
        toastLong("保存完成")

    }

    /**
     * 点击监听
     * @param view View
     */
    fun onClick(view: View) {
        when (view.id) {
            R.id.save_new_report -> {
                report.date = Date()
                saveOrUpdate()
                finish()
            }
            R.id.sel_photo -> {
                Matisse.from(this)
                        .choose(MimeType.ofImage())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(10)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(CaptureStrategy(true, "$packageName.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(GlideEngine())//图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE_PHOYO)//
            }
            R.id.submit_new_report -> {
                report.date ?: let { report.date = Date() }
                if (report.submit) {
                    toastLong("已经提交")
                    return
                }
                submitReport()
            }
            R.id.add_measure -> {
                report.measurementValue.add(0.0)
                measureAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 提交报告
     */
    private fun submitReport() {
        //TODO 检查网络 -> 上传
        ProgressDialog(this).apply {
            setTitle("正在提交...")
            show()
            Handler().postDelayed({
                dismiss()
            }, 2000L)
            setOnDismissListener {
                //模拟提交成功

                report.submit = true
                saveOrUpdate()
                toastLong("提交完成")
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode == REQUEST_CODE_CHOOSE_PHOYO) {//选取图片
            val paths = Matisse.obtainPathResult(data)
            if (paths == null) {
                toastLong("选择失败")
                return
            }
            if (paths.isEmpty()) return

            try {
                report.photoPaths.clear()
                report.photoPaths.addAll(paths)
                Log.d("Debug :", "onActivityResult  ----> $paths")
            } catch (e: Exception) {
                e.printStackTrace()
                toastLong("选择失败 ${e.message}")
            }
            gridAdapter.notifyDataSetChanged()//显示图片
        }
    }
}
