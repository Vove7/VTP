package cn.vove7.vtp

import android.app.Activity
import android.content.Intent
import cn.vove7.vtp.service.ServiceHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

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
}