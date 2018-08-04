package cn.vove7.vtp.asset

import android.content.Context
import cn.vove7.vtp.stream.StreamHelper
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader


/**
 * Asset类
 */
object AssetHelper {

    fun getStrFromAsset(context: Context, fileName: String): String? {
        try {
            val inputReader = InputStreamReader(context.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            return bufReader.readText()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    enum class CopyResult {
        SUCCESS,
        SUCCESS_WITH_REPLACE,
        SUCCESS_WITHOUT_REPLACE,
        FAILED_EXIST,
        FAILED_ASSET_NO_EXIST
    }

    /**
     * 从Asset复制文件到其他路径
     * @param assetFile 相对asset文件路径 eg: html/help.html
     * @param output 输出文件路径
     * @param enforceReplaceIfExist 如果存在目标文件强制替换
     * @param backup 强制替换是否备份原文件 添加后缀.bak
     * @return @see [CopyResult]
     */
    fun copy2File(context: Context, assetFile: String, output: String,
                  enforceReplaceIfExist: Boolean = false, backup: Boolean = false): CopyResult {
        val desFile = File(output)
        val r: CopyResult
        if (desFile.exists()) {//文件存在
            r = when {
                (!enforceReplaceIfExist) -> {
                    return CopyResult.FAILED_EXIST
                }
                (backup) -> {//强制替换
                    desFile.renameTo(File("$output.bak"))
                    CopyResult.SUCCESS_WITH_REPLACE
                }
                else -> {
                    desFile.delete()
                    CopyResult.SUCCESS_WITHOUT_REPLACE
                }
            }
            StreamHelper.copy(context.assets.open(assetFile), FileOutputStream(output))
            return r
        } else {
            if (!desFile.parentFile.exists())
                desFile.mkdirs()
            StreamHelper.copy(context.assets.open(assetFile), FileOutputStream(output))
            return CopyResult.SUCCESS
        }
    }
}