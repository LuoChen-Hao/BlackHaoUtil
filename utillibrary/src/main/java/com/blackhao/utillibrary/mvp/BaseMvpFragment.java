package com.blackhao.utillibrary.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackhao.utillibrary.log.LogHelper;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 16:38
 * Description : Base MVP Fragment (必须在 BaseMvpActivity 中调用)
 */

public abstract class BaseMvpFragment<T extends BaseMvpPresenterImpl> extends Fragment implements BaseMvpViewImpl {

    //用于保存对应 FrameLayout当前显示的 Fragment
    private SparseArray<Fragment> fragmentArray;
    //LogHelper
    public LogHelper log;
    // Presenter
    private T mPresenter;    //Activity
    private BaseMvpActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseMvpActivity) getActivity();
        mPresenter = initPresenter();
        //
        fragmentArray = new SparseArray<>();
        log = activity.log;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initLayoutId(), container, false);
        initUI(view);
        //
        if (mPresenter != null)
            mPresenter.onViewCreate();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
        if (mPresenter != null)
            mPresenter.onViewCreated();
    }

    /**
     * 初始化UI
     */
    protected abstract int initLayoutId();

    /**
     * 初始化UI
     */
    protected abstract void initUI(View view);

    /**
     * UI初始化完成,初始化数据
     */
    protected abstract void initData();

    /**
     * UI初始化完成,初始化接口
     */
    protected abstract void initListener();

    /**
     * 初始化 presenter
     */
    protected abstract T initPresenter();

    /**
     * 获取 presenter
     */
    public T getPresenter() {
        return mPresenter;
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    @Override
    public void showToast(final String str) {
        activity.showToast(str);
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    @Override
    public void showToast(@StringRes int strId) {
        showToast(getString(strId));
    }

    /**
     * 加载框相关
     */
    @Override
    public void showLoading(@StringRes int strId) {
        showLoading(getString(strId));
    }


    @Override
    public void showLoading(String str) {
        activity.showLoading(str);
    }


    @Override
    public void showLoading() {
        activity.showLoading();
    }


    @Override
    public void dismissLoading() {
        activity.dismissLoading();
    }

    /**
     * 替换 fragment
     *
     * @param frameId      frameLayout id
     * @param showFragment 需要显示的 Fragment
     */
    public void replaceFragment(@IdRes int frameId, Fragment showFragment) {
        Fragment currentFragment = fragmentArray.get(frameId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //用于fragment的显示与隐藏
        if (currentFragment != null && currentFragment != showFragment) {
            if (!showFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(currentFragment).add(frameId, showFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(currentFragment).show(showFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            fragmentArray.put(frameId, showFragment);
        } else if (currentFragment == null) {
            if (!showFragment.isAdded()) {    // 先判断是否被add过
                transaction.add(frameId, showFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.show(showFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            fragmentArray.put(frameId, showFragment);
        }
    }

    /**
     * activity 跳转
     *
     * @param target      需要跳转的 Activity
     * @param bundle      bundle
     * @param isCloseSelf 是否关闭当前的 activity
     */
    public void openActivity(Class<?> target, Bundle bundle, boolean isCloseSelf) {
        Intent intent = new Intent(getContext(), target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isCloseSelf) {
            activity.finish();
        }
    }

    /**
     * activity 跳转（不需要传值）
     *
     * @param target 需要跳转的Activity
     */
    public void openActivity(Class<?> target) {
        openActivity(target, null, false);
    }

    /**
     * activity 跳转
     *
     * @param target      需要跳转的 Activity
     * @param bundle      bundle
     * @param requestCode requestCode
     */
    public void openActivityForResult(Class<?> target, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getContext(), target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 生命周期相关
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null)
            mPresenter.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null)
            mPresenter.onStop();
    }

}