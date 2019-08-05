package cn.vove7.vtpdemo

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.vove7.vtp.text.TextHelper
import org.junit.Test
import org.junit.runner.RunWith

/**
 * # Test
 *
 * @author Vove
 * 2019/5/15
 */
@RunWith(AndroidJUnit4::class)
class Test {

    //文本相似度测试
    @Test
    fun textSimilarityTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        arrayOf(
                "猪八戒" to "租八戒",
                "bilibili" to "哔哩哔哩"
        ).forEach {

            val f = TextHelper.compareSimilarityWithPinyin(appContext, it.first, it.second)
            println("$it 相似度：$f")

        }
    }
}