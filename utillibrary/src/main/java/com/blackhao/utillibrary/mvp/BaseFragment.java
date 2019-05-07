package com.blackhao.utillibrary.mvp;

import android.view.View;

/**
 * Author ： BlackHao
 * Time : 2019/4/8 09:11
 * Description : 空白的 Fragment，主要用于不使用 MVP 模式的情况
 */
public abstract class BaseFragment extends BaseMvpFragment<BlankContract.PresenterImpl>
        implements BlankContract.ViewImpl, View.OnClickListener {

    @Override
    protected BlankContract.PresenterImpl initPresenter() {
        return null;
    }
}
