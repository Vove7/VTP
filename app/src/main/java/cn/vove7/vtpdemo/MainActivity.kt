package cn.vove7.vtpdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.vove7.vtp.view.popup.PopupHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            PopupHelper.createTooltipAndShow(this, "125165163", it, 3000)
        }
    }
}
