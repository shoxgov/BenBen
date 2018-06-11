/**
 * author: wsy
 * createtime: 2014年7月2日
 */
package com.benben.bb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 重写TextView 为了使ListView中的TextView一直获得焦点而可以跑马灯
 * 
 * @author wsy
 * 
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context con) {
		super(con);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true; // 设置焦点为True，即可保持TextView一直获取焦点
	}
}
