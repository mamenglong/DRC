package com.mml.drc.activities

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mml.drc.Model.BaseResponse
import com.mml.drc.Model.Report
import com.mml.drc.R
import com.mml.drc.adapter.MeasureTextAdapter
import com.mml.drc.adapter.ReportImageGridAdapter
import com.mml.drc.utils.*
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_new_report.*
import org.litepal.LitePal
import java.util.*
import kotlin.concurrent.thread

class NewReportActivity : AppCompatActivity() {

    private lateinit var report: Report//TODO 操作员
    private val gridAdapter by lazy { ReportImageGridAdapter(this, report.photoPaths) }
    private val measureAdapter by lazy { MeasureTextAdapter(this, report.measurementValue) }


    companion object {
        const val ERR_ID = -111L
        const val REQUEST_CODE_CHOOSE_PHOYO = 1000
    }

    private fun disableView() {//浏览模式
        action_layout.visibility = View.GONE
        gridAdapter.disableAll()
        measureAdapter.disableAll()
    }

    var id: Long = ERR_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_report)

        id = intent?.getLongExtra("id", ERR_ID) ?: ERR_ID
        //实例化Report
        try {
            report = if (id != ERR_ID) {//编辑
                LitePal.find(Report::class.java, id)
            } else Report()
            report.fill() // 填数据
        } catch (e: Throwable) {//新建
            e.printStackTrace()
            toastLong("程序错误")
            finish()
        }

        requestPermission()
        measure_list_view.adapter = measureAdapter
        report_photo_grid.adapter = gridAdapter
        report_photo_grid.numColumns = 1
        if (id != ERR_ID && report.submit)
            Handler().postDelayed({
                disableView()
            }, 1000)

        val s = LitePal.findAll(Report::class.java)
        Log.d("Debug :", "onCreate 报告数量 ----> ${s.size}")
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("确认退出?")
            setMessage("内容将不会被保存")
            setPositiveButton("取消") { d, _ ->
                d.dismiss()
            }
            setNegativeButton("确认") { d, _ ->
                d.dismiss()
                super.onBackPressed()
            }
            show()
        }
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
            report.update()//更新
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
                if (report.submit) {
                    toastLong("已经提交，无法保存")
                    return

                }
                saveOrUpdate()
                finish()
            }

            R.id.submit_new_report -> {
                report.date ?: let { report.date = Date() }
                if (report.submit) {
                    toastLong("已经提交，无法再次提交")
                    return
                }
                if (report.isEmpty) {
                    toastShort("填写数据")
                    return
                }
                submitReport()
            }
//            R.id.add_measure -> {
//                report.measurementValue.add("")
//                measureAdapter.notifyDataSetChanged()
//            }
        }
    }

    /**
     * 提交报告
     */
    var d: Dialog? = null

    private fun submitReport() {
        if (!isNetworkAvailable(this)) {
            toastShort("网络不可用,请先保存")
            return
        }


        d = ProgressDialog(this).apply {
            setTitle("正在提交...")
            show()
            thread {
                doSubmit()
            }
        }

    }

    private fun doSubmit() {
        //压缩图片
        val fs = compassImages(this, report.photoPaths.filter { it != null })
        NetHelper.postFile<BaseResponse>(Urls.INSERT_REPORT, report, files = fs) {
            success { _, responseModel ->
                Log.d("Debug :", "doSubmit  ----> $responseModel")
                if (responseModel.status == 1) {
                    onSubmitSuccess()
                } else {
                    toastShort(responseModel.msg ?: "未知错误")
                }
                d?.dismiss()
            }
            fail { _, e ->
                toastShort(e.message ?: "未知错误")
                d?.dismiss()
            }
        }
    }

    private fun onSubmitSuccess() {
        report.submit = true
        saveOrUpdate()
        toastLong("提交成功")
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode > 999) {//选取图片
            val paths = Matisse.obtainPathResult(data)
            if (paths == null) {
                toastLong("选择失败")
                return
            }
            if (paths.isEmpty()) return

            val pos = requestCode - 1000
            report.photoPaths[pos] = paths[0]
            gridAdapter.notifyDataSetChanged()//显示图片
        }
    }
}
