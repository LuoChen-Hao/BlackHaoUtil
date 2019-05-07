package com.blackhao.utillibrary.mvp;

/**
 * Author ： BlackHao
 * Time : 2019/4/8 09:11
 * Description : 空白的 activity，主要用于不使用 MVP 模式的情况
 */
public abstract class BaseActivity extends BaseMvpActivity<BlankContract.PresenterImpl>
        implements BlankContract.ViewImpl{

    @Override
    public int getStatusMode() {
        return TRANSLUCENT_STATUS;
    }


    @Override
    protected BlankContract.PresenterImpl initPresenter() {
        return null;
    }
}
