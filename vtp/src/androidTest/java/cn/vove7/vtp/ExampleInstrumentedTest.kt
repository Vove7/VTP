package cn.vove7.vtp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.vove7.vtp.text.TextHelper
import cn.vove7.vtp.text.TextTransHelper

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("cn.vove7.vtp", appContext.packageName)
    }
    @Test
    fun transformWords2Chinese() {
        val wws = arrayOf(
                "你好",
                "刘学他",
                "刘薛涛",
                "刘xuetao"
        )
        wws.forEach {
            println(TextHelper.chineseStr2Pinyin(it))
        }
    }

    @Test
    fun compareSimilarityTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val tests = hashMapOf(
                Pair("刘雪", "刘雪ta"),
                Pair("你好", "您好"),
                Pair("Qq", "qq"),
                Pair("点击RUN", "RUN")


        )
        tests.forEach {
            println("""${it.key} ${it.value}""")
            println(TextHelper.compareSimilarityWithPinyin(appContext,it.key, it.value))
            println()
        }
    }

    @Test
    fun testChinese2First() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val p=TextTransHelper(appContext)
        arrayOf(
                "一二三123",
                "i我和欧文h",
                "吗朦胧"
        ).forEach {
            println(p.chineseStr2Pinyin(it,true))
        }
    }
}
