package cn.vove7.vtp.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringRes


class SpHelper {

    private val context: Context
    /**
     * 存储文件名
     */
    private var spName: String? = null
    private lateinit var preferences: SharedPreferences

    constructor(context: Context) {
        this.context = context
        init()
    }

    constructor(context: Context, spName: String?) {
        this.context = context
        this.spName = spName
        init()
    }


    private fun init() {
        preferences = if (spName == null)
            PreferenceManager.getDefaultSharedPreferences(context)
        else
            context.getSharedPreferences(spName, MODE_PRIVATE)
    }

    private fun s(@StringRes id: Int): String {
        return context.getString(id)
    }

    /**
     * @param keyId @StringRes keyId
     * @return null when have no this keyValue
     */
    fun getString(@StringRes keyId: Int): String? {
        return getString(s(keyId))
    }

    /**
     * @param key key
     * @return null when have no this keyValue
     */
    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    /**
     * @param keyId @StringRes keyId
     * @return null when have no this keyValue
     */
    fun getInt(@StringRes keyId: Int): Int {
        return getInt(s(keyId))
    }

    /**
     * @param key key
     * @return null when have no this keyValue
     */
    fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    /**
     * @param keyId @StringRes keyId
     * @return null when have no this keyValue
     */
    fun getLong(@StringRes keyId: Int): Long {
        return getLong(s(keyId))
    }

    /**
     * @param key key
     * @return null when have no this keyValue
     */
    fun getLong(key: String): Long {
        return preferences.getLong(key, -1L)
    }

    /**
     * @param keyId @StringRes keyId
     * @return null when have no this keyValue
     */
    fun getFloat(@StringRes keyId: Int): Float {
        return getFloat(s(keyId))
    }

    /**
     * @param key key
     * @return null when have no this keyValue
     */
    fun getFloat(key: String): Float {
        return preferences.getFloat(key, -1.0f)
    }

    /**
     * @param keyId @StringRes keyId
     * @return false when have no this keyValue
     */
    fun getBoolean(@StringRes keyId: Int): Boolean {
        return getBoolean(s(keyId))
    }

    /**
     * @param key key
     * @return false when have no this keyValue
     */
    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     *
     * @param keyId @StringRes keyId
     * @return if exists Set<String> ,else null
     */
    fun getStringSet(@StringRes keyId: Int): Set<String>? {
        return getStringSet(s(keyId))
    }

    /**
     *
     * @param key key
     * @return if exists Set<String> ,else null
     */
    fun getStringSet(key: String): Set<String>? {
        return preferences.getStringSet(key, null)
    }

    /**
     *
     * @param keyId @StringRes keyId
     */
    fun set(@StringRes keyId: Int, value: Any) {
        set(s(keyId), value)
    }

    fun set(key: String, value: Any) {
        val editor = preferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is String -> editor.putString(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> throw Exception("值类型不支持 : ${value::class.java.name}")
        }
        editor.apply()
    }
}
