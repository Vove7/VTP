package cn.vove7.vtp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * 自适应高度ListView
 * 常用于内嵌与ScrollView
 */
class ListViewInScrollView : ListView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }


}
