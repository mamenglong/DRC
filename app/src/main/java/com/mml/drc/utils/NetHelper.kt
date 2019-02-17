package com.mml.drc.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


/**
 * # NetHelper
 *
 * @author 17719247306
 * 2018/9/12
 */

object NetHelper {


    var timeout = 15L
    /**
     * 网络post请求 内容格式为json
     * @param url String
     * @param model BaseRequestModel<*>?
     * @param requestCode Int
     * @param callback OnResponse<T>
     */
    inline fun <reified T> postFile(
            url: String, model: Any,
            requestCode: Int = 0, files: Array<File>
            , callback: WrappedRequestCallback<T>.() -> Unit
    ) {
        val client = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS).build()

        val json = GsonHelper.toJson(model)

        val mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流

        val multiBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            multiBuilder.addFormDataPart("json_data",  json)//json

        //文件
        Log.d("Debug :", "postFile 文件个数 ----> ${files.size}")
        files.forEach {
            val fileBody = RequestBody.create(mediaType, it)//把文件与类型放入请求体
            multiBuilder.addFormDataPart("multipartFile", it.name, fileBody)
        }
        val requestBody = multiBuilder.build()
        val request = Request.Builder().url(url)
                .post(requestBody)
                .build()
        val call = client.newCall(request)
        call(call, requestCode, callback)
    }

    /**
     * 网络post请求 内容格式为json
     * @param url String
     * @param model BaseRequestModel<*>?
     * @param requestCode Int
     * @param callback OnResponse<T>
     */
    inline fun <reified T> postJson(
            url: String, model: Any,
            requestCode: Int = 0, callback: WrappedRequestCallback<T>.() -> Unit
    ) {
        val client = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS).build()

        val json = GsonHelper.toJson(model)
        val requestBody = FormBody.create(MediaType
                .parse("application/json; charset=utf-8"), json)

        val request = Request.Builder().url(url)
                .post(requestBody)
                .build()
        val call = client.newCall(request)
        call(call, requestCode, callback)
    }

    inline fun <reified T> call(
            call: Call, requestCode: Int = 0,
            cb: WrappedRequestCallback<T>.() -> Unit
    ) {
        val callback = WrappedRequestCallback<T>()
        cb.invoke(callback)
        thread {
            prepareIfNeeded()
            val handler = Handler(Looper.getMainLooper())
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    handler.post {
                        callback.onFailed(requestCode, e)
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {//响应成功更新UI
                    if (response.isSuccessful) {
                        val s = response.body()?.string()
                        try {
                            Log.d("", "onResponse --->\n$s")
                            val bean: T?
                            try {
                                bean = GsonHelper.fromResponseJson<T>(s)!!
                                handler.post {
                                    callback.onSuccess(requestCode, bean)
                                }
                            } catch (e: Exception) {
                                handler.post {
                                    callback.onFailed(requestCode, e)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            handler.post {
                                callback.onFailed(requestCode, e)
                            }
                        }
                    } else handler.post {
                        callback.onFailed(requestCode, Exception("请求失败${response.message()}"))
                    }
                }
            })
        }
    }

    fun prepareIfNeeded() {
        if (Looper.myLooper() == null)
            Looper.prepare()
    }

    inline fun <reified T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }

}

interface RequestCallback<T> {
    fun onSuccess(requestCode: Int, data: T)
    fun onFailed(requestCode: Int, e: Exception)
}

class WrappedRequestCallback<T> : RequestCallback<T> {
    private var _OnSuccess: ((Int, T) -> Unit)? = null
    private var _OnFailed: ((Int, Exception) -> Unit)? = null
    override fun onSuccess(requestCode: Int, data: T) {
        _OnSuccess?.invoke(requestCode, data)
    }

    override fun onFailed(requestCode: Int, e: Exception) {
        _OnFailed?.invoke(requestCode, e)
    }

    fun success(s: (Int, T) -> Unit) {
        _OnSuccess = s
    }

    fun fail(s: (Int, Exception) -> Unit) {
        _OnFailed = s
    }
}
