package com.keyboard.funstickers;

/**
 * Created by Shalu Dhochak on 5/8/2018.
 */

public enum CustomPagerEnum {

    FOOD(R.string.food, R.layout.food),
    INTIMATE_WORDS(R.string.intimate_words, R.layout.intimate_words),
    LETS_GO(R.string.lets_go, R.layout.lets_go),
    YOU_ARE(R.string.you_are, R.layout.you_are);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }


    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
