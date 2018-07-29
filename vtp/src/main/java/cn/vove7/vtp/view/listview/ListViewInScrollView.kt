package cn.vove7.vtp.view.listview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * 自适应高度ListView
 * 常用于内嵌与ScrollView
 */
class ListViewInScrollView (context: Context, p_attrs: AttributeSet) : WrapContentListView(context, p_attrs)