package com.phc.neckrreferential.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/15 07
 * 描述：
 */
public class KeyboardUtils {
    public static void hide(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static void show(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view,0);
    }

}
