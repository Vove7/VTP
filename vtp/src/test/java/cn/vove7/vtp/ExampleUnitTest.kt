package cn.vove7.vtp

import cn.vove7.vtp.log.Vog
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun className() {
        println(ExampleUnitTest::class.java.name)
    }

    @Test
    fun double2Float() {
        val f = 1.111
        println(f.toFloat())
        Vog.d("debug")
        Vog.e("wr")
        Vog.i("wr")

    }
}