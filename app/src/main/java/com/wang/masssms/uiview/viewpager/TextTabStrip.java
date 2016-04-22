package com.wang.masssms.uiview.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * tab为文字
 * Created by huangjinxin on 15/4/28.
 */
public class TextTabStrip extends BaseTabStrip {
    public TextTabStrip(Context context) {
        super(context);
    }

    public TextTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView){
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                if(i == currentPosition){
                    tab.setTextColor(tabTextSelectedColor);
                } else {
                    tab.setTextColor(tabTextColor);
                }
            }
        }
    }

    @Override
    protected void genrateAndAddTab() {
        super.genrateAndAddTab();
        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            TextView tab = new TextView(getContext());
            tab.setText(pager.getAdapter().getPageTitle(i));
            tab.setGravity(Gravity.CENTER);
            tab.setSingleLine();
            tab.setFocusable(true);

            final int position = i;

            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(position);
                }
            });

            tab.setPadding(tabPadding, 0, tabPadding, 0);
            tabsContainer.addView(tab, i, defaultTabLayoutParams);
        }
    }
}
