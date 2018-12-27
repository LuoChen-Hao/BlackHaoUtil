package com.blackhao.utillibrary.mvp;

import android.support.annotation.StringRes;

/**
 * Author ： BlackHao
 * Time : 2018/12/19 19:11
 * Description : View 层基类
 */
public interface BaseMvpViewImpl {
    /**
     * Toast相关
     */
    void showToast(@StringRes int strId);

    void showToast(String str);

    /**
     * 加载框相关
     */
    void showLoading(@StringRes int strId);

    void showLoading(String str);

    void showLoading();

    void dismissLoading();
}
