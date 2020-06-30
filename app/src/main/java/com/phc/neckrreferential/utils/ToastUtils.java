package com.phc.neckrreferential.utils;

import android.widget.Toast;

import com.phc.neckrreferential.base.BaseApplication;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/29 11
 * 描述：
 */
public class ToastUtils {

    private static Toast sToast;

    public static void showToast(String tips){
        if (sToast == null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }else {
            sToast.setText(tips);
        }
        sToast.show();


    }
}
