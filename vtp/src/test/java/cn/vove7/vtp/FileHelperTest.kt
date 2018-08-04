package cn.vove7.vtp

import cn.vove7.vtp.stream.StreamHelper
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * # FileHelperTest
 *
 * @author SYSTEM
 * 2018/8/3
 */
class FileHelperTest {
    /**
     * Test Result : StreamHelper.copy > File.appendBytes
     */
    @Test
    fun testEasyCopy() {//80M
        val f = "F:/log.txt"
        val t = "F:/log1.txt"
        val t2 = "F:/log2.txt"

        val b = System.currentTimeMillis()
        StreamHelper.copy(FileInputStream(f), FileOutputStream(t2))
        val b2 = System.currentTimeMillis()
        println(b2 - b)
        val tt = File(t)
        tt.delete()
        tt.appendBytes(File(f).readBytes())
        val b3 = System.currentTimeMillis()
        println(b3 - b2)
    }
}