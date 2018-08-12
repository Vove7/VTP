package cn.vove7.vtpdemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import cn.vove7.vtp.contact.ContactHelper
import cn.vove7.vtp.text.TextTransHelper
import cn.vove7.vtp.view.popup.PopupHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            ContactHelper.getAllContacts(this)


        val p = TextTransHelper(this)
        arrayOf(
                "啊啊啊",
                "哈哈哈卡卡哪里n",
                "测试欸了鳄梨i；"
        ).forEach {
            val r = p.chineseStr2Pinyin(it, true)
            trans.append(r + '\n')
            Log.d("Debug :", "onCreate  ----> $r")
        }


        btn.setOnClickListener {
            PopupHelper.createTooltipAndShow(this, "125165163", it, 3000)
        }
    }
}
