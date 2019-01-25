package com.mml.drc.activities

import android.content.Intent
import android.os.Bundle
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

    val report = Report()
    val listAdapter by lazy { NewReportListAdapter(this, report.reportItems) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_report)
        report_listview.adapter = listAdapter

        requestPermiss()

        val s = LitePal.findAll(Report::class.java)
        Log.d("Debug :", "onCreate 报告数量 ----> ${s.size}")
    }


    private fun requestPermiss() {
        ActivityCompat.requestPermissions(this, arrayOf(
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
        ), 9)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.save_new_report -> {
                report.date = Date()
                if (!report.check()) {
                    toast("请输入完整信息")
                    return
                }
                report.save()
                toastLong("保存完成")
                finish()
            }
            R.id.submit_new_report -> {
                report.date = Date()
                if (!report.check()) {
                    toast("请输入完整信息")
                    return
                }
                if (report.submit) {
                    toastLong("已经提交")
                } else {
                    report.submit = true
                    report.save()
                }
            }
            R.id.add_item -> {
                report.addItem()
                listAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode > 999) {//选取图片 code = 1000
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
