package cn.vove7.vtp.stream

import java.io.InputStream
import java.io.OutputStream

/**
 * @author SYSTEM
 *
 * 2018/8/3
 */
object StreamHelper {

    /**
     * InputStream -> OutputStream
     */
    fun copy(inputStream: InputStream, out: OutputStream): Boolean {
        inputStream.use { input ->
            out.also {
                var byteRead: Int = -1
                val buffer = ByteArray(1024 * 1024)
                while (input.read(buffer).also { byteRead = it } != -1) {
                    it.write(buffer, 0, byteRead)
                }
            }.close()
            return true
        }
    }
}