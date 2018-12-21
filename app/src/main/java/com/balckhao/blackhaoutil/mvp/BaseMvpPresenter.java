package com.balckhao.blackhaoutil.mvp;

import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 09:05
 * Description : Presenter 封装
 */
public abstract class BaseMvpPresenter<T extends BaseMvpViewImpl> implements BaseMvpPresenterImpl {

    /**
     * 弱引用持有
     */
    private WeakReference<T> mView;
    private WeakReference<Context> mContext;

    public BaseMvpPresenter(Context context, T mView) {
        this.mView = new WeakReference<>(mView);
        this.mContext = new WeakReference<>(context);
    }


    public T getView() {
        return mView.get();
    }

    public Context getContext() {
        return mContext.get();
    }

    @Override
    public void onViewCreate() {

    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

}
