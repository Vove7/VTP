package cn.vove7.vtp.file

import cn.vove7.vtp.stream.StreamHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 *
 *
 * Created by Vove on 2018/6/21
 */
object FileHelper {

    /**
     * 不带检查复制
     */
    fun easyCopy(from: String, to: String) {
        StreamHelper.copy(FileInputStream(from), FileOutputStream(to))
    }

    /**
     * 不带检查复制
     */
    fun easyCopy(from: File, to: File) {
        StreamHelper.copy(FileInputStream(from), FileOutputStream(to))
    }


    /**
     * 获得文件直观大小
     * 进制：1024
     * 暂时最大转换单位 ***Tb
     * @param units 自定义单位值
     */
    fun getAdapterFileSize(size: Long, units: Array<String> = sizeUnits): String {
        var i = 0
        while (size < 10L && i < sizeUnits.size) {
            size shl 10
            i++
        }
        return "$size${units[i]}"
    }

    /**
     * 计算文件MD5
     */
    fun fileMd5(file: File): String = cal(file, "Md5")
    /**
     * 计算文件SHA1
     */
    fun fileSha1(file: File): String = cal(file, "SHA-1")

    private fun cal(file: File, a: String): String {

        file.inputStream().use { input ->

            val buffer = ByteArray(8192)
            var len: Int = -1
            val digest = MessageDigest.getInstance(a)
            while (input.read(buffer).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            val bigInt = BigInteger(1, digest.digest())
            return bigInt.toString(16)
        }
    }

    private val sizeUnits = arrayOf("bytes", "KB", "MB", "GB", "TB")
}