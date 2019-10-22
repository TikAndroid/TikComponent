package com.tik.android.component.widget;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/15
 */
public enum SettingItemStyle {
    SINGLE(0), TOP(1), MID(2), BOTTOM(3);
    int style;
    SettingItemStyle(int v) {
        style = v;
    }
    public static SettingItemStyle fromValue(int value) {
        for (SettingItemStyle itemStyle : values()) {
            if(itemStyle.style == value) {
                return itemStyle;
            }
        }
        return SINGLE;
    }
}
