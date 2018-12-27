package com.blackhao.utillibrary.mvp;

/**
 * Author ： BlackHao
 * Time : 2018/12/19 19:48
 * Description : Presenter 封装
 */
public interface BaseMvpPresenterImpl {

    /**
     * 开始创建 View
     */
    void onViewCreate();

    /**
     * View 创建完成
     */
    void onViewCreated();

    /**
     * Activity / fragment 生命周期
     */
    void onResume();

    void onStart();

    void onPause();

    void onStop();

    void onDestroy();
}
