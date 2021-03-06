package com.mml.drc.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * # GsonHelper
 *
 * @author Administrator
 * 2018/9/19
 */
object GsonHelper {
    private val builder = GsonBuilder()

    init {
        builder.serializeSpecialFloatingPointValues()
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
        builder.addSerializationExclusionStrategy(object : ExclusionStrategy {

            override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                val expose = fieldAttributes.getAnnotation(Expose::class.java)
                return expose != null && !expose.serialize
            }

            override fun shouldSkipClass(aClass: Class<*>): Boolean = false
        }).addDeserializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                val expose = fieldAttributes.getAnnotation(Expose::class.java)
                return expose != null && !expose.deserialize
            }

            override fun shouldSkipClass(aClass: Class<*>): Boolean = false
        }).disableHtmlEscaping()
    }

    fun toJson(model: Any?): String {
        if (model == null) return ""
        return builder.create().toJson(model)
    }

    fun prettyJson(model: Any?): String {
        if (model == null) return ""
        return builder.setPrettyPrinting().create().toJson(model)
    }

    /**
     * 服务返回Json解析
     * @param s String?
     * @param type Type
     * @return ResponseMessage<T>?
     */
    fun <T> fromJsonObj(s: String?, type: Type): T? {
        val bean = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create().fromJson<T>(s, type)
        return bean
    }

    /**
     * 服务返回Json解析
     * @param s String?
     * @param type Type
     * @return ResponseMessage<T>?
     */
    inline fun <reified T> fromResponseJson(s: String?): T? {
        val type = getType<T>()
        val bean = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create().fromJson<T>(s, type)
        return bean
    }

    inline fun <reified T> fromJson(s: String?): T? {
        val type = getType<T>()
        val bean = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create().fromJson<T>(s, type)
        return bean
    }

    inline fun <reified T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }


}