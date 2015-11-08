package com.example.wmakaben.caloriecounter;

public enum CustomPagerEnum {

    RED(R.string.camera_fragment_title, R.layout.fragment_camera),
    BLUE(R.string.history_fragment_title, R.layout.fragment_history);

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