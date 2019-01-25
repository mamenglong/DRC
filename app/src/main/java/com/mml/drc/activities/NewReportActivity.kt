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
import com.mml.drc.adapter.NewReportListAdapter
import com.mml.drc.utils.toast
import com.mml.drc.utils.toastLong
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_new_report.*
import org.litepal.LitePal
import java.util.*

class NewReportActivity : AppCompatActivity() {

    private lateinit var report: Report//TODO 操作员
    private val listAdapter by lazy { NewReportListAdapter(this, report.reportItems) }

    companion object {
        const val ERR_ID = -111L
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
        report_listview.adapter = listAdapter

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
        if (id != ERR_ID) {
            report.save()
        } else {
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
                if (!report.check()) {
                    toast("请输入完整信息")
                    return
                }
                saveOrUpdate()
                finish()
            }
            R.id.submit_new_report -> {
                report.date ?: let { report.date = Date() }
                if (!report.check()) {
                    toast("请输入完整信息")
                    return
                }
                if (report.submit) {
                    toastLong("已经提交")
                    return
                }
                submitReport()
            }
            R.id.add_item -> {
                report.addItem()
                listAdapter.notifyDataSetChanged()
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
            }, 3000L)
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
        if (requestCode > 999) {//选取图片 code = 1000+pos
            val paths = Matisse.obtainPathResult(data)
            if (paths == null) {
                toastLong("选择失败")
                return
            }
            if (paths.isEmpty()) return
            val pos = requestCode - 1000

            try {
                val p = paths[0]
                report.reportItems[pos].imagePath = p
                Log.d("Debug :", "onActivityResult  ----> $p")
            } catch (e: Exception) {
                e.printStackTrace()
                toastLong("选择失败 ${e.message}")
            }
            listAdapter.notifyDataSetChanged()//显示图片
        }
    }
}
