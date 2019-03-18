package cn.vove7.vtp.net

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * # GsonHelper
 *
 * @author Administrator
 * 2018/9/19
 */
object GsonHelper {

    /**
     * 被[GsonIgnore]标记的属性，不会被序列化
     */
    val builder
        get() = GsonBuilder().apply {
            serializeSpecialFloatingPointValues()
            setDateFormat("yyyy-MM-dd HH:mm:ss")
            addSerializationExclusionStrategy(object : ExclusionStrategy {

                override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                    val ignore = fieldAttributes.getAnnotation(GsonIgnore::class.java)
                    return ignore != null
                }

                override fun shouldSkipClass(aClass: Class<*>): Boolean = false
            }).addDeserializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(fieldAttributes: FieldAttributes): Boolean {
                    val ignore = fieldAttributes.getAnnotation(GsonIgnore::class.java)
                    return ignore != null
                }

                override fun shouldSkipClass(aClass: Class<*>): Boolean = false
            }).disableHtmlEscaping()
        }

    /**
     * 对象转json
     * @param model Any?
     * @param pretty Boolean
     * @return String
     */
    fun toJson(model: Any?, pretty: Boolean = false): String {
        if (model == null) return ""
        val b = builder
        if (pretty) b.setPrettyPrinting()
        return b.create().toJson(model)
    }


    /**
     * Json实例化
     * @param s String?
     * @return T?
     */
    @Throws
    inline fun <reified T> fromJson(s: String?): T? {
        val type = getType<T>()
        return builder
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create().fromJson<T>(s, type)
    }

    inline fun <reified T> getType(): Type {
        return object : TypeToken<T>() {}.type
    }

}

/**
 * 扩展函数
 * @receiver Any
 * @return String
 */
fun Any.toJson(): String {
    return GsonHelper.toJson(this)
}
