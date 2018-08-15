package cn.vove7.vtp.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import cn.vove7.vtp.log.Vog


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
        return preferences.getString(s(keyId), null)
    }

    /**
     * @param keyId @StringRes keyId
     * @return false when have no this keyValue
     */
    fun getBoolean(@StringRes keyId: Int): Boolean {
        return preferences.getBoolean(s(keyId), false)
    }

    /**
     *
     * @param keyId @StringRes keyId
     * @return if exists Set<String> ,else null
     */
    fun getStringSet(@StringRes keyId: Int): Set<String>? {
        return preferences.getStringSet(s(keyId), null)
    }

    fun setValue(@StringRes keyId: Int, value: Any) {
        setValue(s(keyId), value)
    }

    private fun setValue(key: String, value: Any) {
        val editor = preferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is String -> editor.putString(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> Vog.e(this, "设置值类型出错 key: $key")
        }
        editor.apply()
    }
}
