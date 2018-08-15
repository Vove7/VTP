package cn.vove7.vtp.builder

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import java.io.Serializable

/**
 * # BundleBuilder
 * A builder of [Bundle]
 */
class BundleBuilder {
    val data = Bundle()
    /**
     * 支持：
     * String
     * Long
     * Byte
     * ByteArray
     * Bundle
     * CharArray
     * Char
     * Int
     * IntArray
     * Float
     * FloatArray
     * Double
     * DoubleArray
     * Short
     * ShortArray
     * not Size
     * Serializable
     * CharSequence
     * IBinder
     * Parcelable
     */
    fun put(key: String, v: Any): BundleBuilder {
        when (v) {
            is String -> data.putString(key, v)
            is Long -> data.putLong(key, v)
            is Byte -> data.putByte(key, v)
            is ByteArray -> data.putByteArray(key, v)
            is Bundle -> data.putBundle(key, v)
            is CharArray -> data.putCharArray(key, v)
            is Char -> data.putChar(key, v)
            is Int -> data.putInt(key, v)
            is IntArray -> data.putIntArray(key, v)
            is Float -> data.putFloat(key, v)
            is FloatArray -> data.putFloatArray(key, v)
            is Double -> data.putDouble(key, v)
            is DoubleArray -> data.putDoubleArray(key, v)
            is Short -> data.putShort(key, v)
            is ShortArray -> data.putShortArray(key, v)
            is Serializable -> data.putSerializable(key, v)
            is CharSequence -> data.putCharSequence(key, v)
            is IBinder -> data.putBinder(key, v)
            is Parcelable -> data.putParcelable(key, v)
            else -> throw Exception("不支持的类型: ${v::class.java.simpleName}")
        }
        return this
    }

    fun putParcelableArrayList(k: String, list: ArrayList<Parcelable>): BundleBuilder {
        data.putParcelableArrayList(k, list)
        return this
    }

    fun putStringArrayList(k: String, v: ArrayList<String>): BundleBuilder {
        data.putStringArrayList(k, v)
        return this
    }

    fun putIntArrayList(k: String, v: ArrayList<Int>): BundleBuilder {
        data.putIntegerArrayList(k, v)
        return this
    }

    fun put(b: Bundle): BundleBuilder {
        data.putAll(b)
        return this
    }

    fun get(): Bundle = data
    fun build(): Bundle = data
}