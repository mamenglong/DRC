package com.mml.drc.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**
 * # WrapContentGridView
 *
 * @author 11324
 * 2019/1/25
 */
class WrapContentGridView(context: Context, p_attrs: AttributeSet) : GridView(context, p_attrs) {
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, mExpandSpec)
    }
}