package com.mml.drc.utils

/**
 * # utils
 *
 * @author 11324
 * 2019/2/17
 */
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.concurrent.CountDownLatch


/**
 * 检查网络是否可用
 * @param context
 * @return
 */
fun isNetworkAvailable(context: Context): Boolean {
    val manager = (context
            .applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager?) ?: return false

    val networkInfo = manager.activeNetworkInfo
    return (networkInfo?.isConnected ?: false) && (networkInfo?.isAvailable ?: false)
}

/**
 * 同步压缩图片
 * @param context Context
 * @param sFiles List<String>
 * @return Array<File>
 */
fun compassImages(context: Context, sFiles: List<String>): Array<File> {
    val cd = CountDownLatch(sFiles.size)
    val outFiles = arrayListOf<File>()
    sFiles.forEach {
        Luban.with(context).load(it)
                .setTargetDir(context.externalCacheDir?.absolutePath
                    ?: context.cacheDir.absolutePath)
                .setCompressListener(object : OnCompressListener {
                    override fun onSuccess(file: File?) {
                        cd.countDown()
                        Log.d("Debug :", "onStart  ----> 压缩完成$it -> 输出: ${file?.absolutePath}")
                        outFiles.add(file ?: File(it))
                    }

                    override fun onError(e: Throwable?) {
                        cd.countDown()
                        outFiles.add(File(it))
                        e?.printStackTrace()
                        Log.d("Debug :", "onStart  ----> 压缩失败$it")
                    }

                    override fun onStart() {
                        Log.d("Debug :", "onStart  ----> 开始压缩$it")
                    }
                })
                .launch()
    }
    cd.await()//等待压缩完成
    return outFiles.toTypedArray()
}