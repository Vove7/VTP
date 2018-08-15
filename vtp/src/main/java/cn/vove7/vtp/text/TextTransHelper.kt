package cn.vove7.vtp.text

import android.content.Context
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

/**
 * # TextTransHelper
 *
 * @author Vove
 * 2018/8/8
 */
class TextTransHelper(val context: Context) {
    /**
     * 汉字转拼音
     * 一字多音？😔
     */
    fun chineseStr2Pinyin(words: String, onlyFirst: Boolean = false): String {
        var pinyinStr = ""
        val ph = PinyinHelper.init(context)
        val newChar = words.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (c in newChar) {
            pinyinStr += if (c.toInt() > 128) {
                try {
                    val all = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]
                    if (onlyFirst) "${all[0]}" else all
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    println(e.message ?: e.toString())
                    c
                } catch (ne: NullPointerException) {
                    println(ne.message ?: ne.toString())
                    c
                }
            } else {
                c
            }
        }
        return pinyinStr
    }
}