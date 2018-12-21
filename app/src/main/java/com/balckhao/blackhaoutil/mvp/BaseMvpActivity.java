package com.balckhao.blackhaoutil.mvp;

import android.annotation.StringRes;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.balckhao.blackhaoutil.base.LoadingDialog;
import com.balckhao.blackhaoutil.logUtil.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 16:38
 * Description : Base MVP Activity
 */

public abstract class BaseMvpActivity<T extends BaseMvpPresenterImpl> extends AppCompatActivity implements BaseMvpViewImpl {

    //Handler
    public WeakHandler handler;
    //用于保存对应 FrameLayout当前显示的 Fragment
    private SparseArray<Fragment> fragmentArray;
    //LogUtil
    public LogUtil log;
    //加载框
    private LoadingDialog loadingDialog;
    // Presenter
    private T mPresenter;
    //使用状态栏模式
    private int statusMode = -1;
    /**
     * 不使用状态栏
     */
    public static int NO_STATUS = -1;
    /**
     * 沉浸式状态栏
     */
    public static int TRANSLUCENT_STATUS = 1;
    /**
     * 隐藏状态栏
     */
    public static int HIDE_STATUS = 2;
    /**
     * 全屏（隐藏状态栏和导航栏）
     */
    public static int FULL_SCREEN_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化Log
        log = LogUtil.getInstance();
        //初始化fragmentArray
        fragmentArray = new SparseArray<>();
        //
        loadingDialog = new LoadingDialog();
        //获取presenter
        mPresenter = initPresenter();
        if (mPresenter != null)
            mPresenter.onViewCreate();
        //初始化UI
        initUI();
        //初始化weakHandler
        initHandler();
        //隐藏 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //更改状态栏模式
        statusMode = getStatusMode();
        if (statusMode == TRANSLUCENT_STATUS) {
            translucentStatus();
        } else if (statusMode == HIDE_STATUS) {
            hideStatusBar();
        }
        //view 初始化完成
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                //
                if (mPresenter != null)
                    mPresenter.onViewCreated();
                //初始化数据
                initData();
                //添加listener
                initListener();
            }
        });
    }

    /**
     * 初始化UI
     */
    protected abstract void initUI();

    /**
     * UI初始化完成,初始化数据
     */
    protected abstract void initData();

    /**
     * UI初始化完成,初始化接口
     */
    protected abstract void initListener();

    /**
     * 子类要处理 handler的消息，重写该方法
     */
    protected abstract void handleMessage(Message msg);

    /**
     * 初始化 Handler
     */
    private void initHandler() {
        handler = new WeakHandler(this);
    }

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
     * 获取状态栏模式（默认无状态栏模式，修改状态栏模式只需重写该方法）
     */
    public int getStatusMode() {
        return NO_STATUS;
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    @Override
    public void showToast(final String str) {
        if (!isUIThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
            return;
        }
        Toast.makeText(BaseMvpActivity.this, str, Toast.LENGTH_SHORT).show();
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
    public void showLoading(final String str) {
        if (!isUIThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showLoading(str);
                }
            });
            return;
        }
        log.e("show : " + str);
        loadingDialog.setLoadingTxt(str);
        loadingDialog.show(getSupportFragmentManager(), "loading");
    }


    @Override
    public void showLoading() {
        showLoading("");
    }


    @Override
    public void dismissLoading() {
        if (!isUIThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dismissLoading();
                }
            });
            return;
        }
        loadingDialog.dismiss();
    }

    /**
     * 替换 fragment
     *
     * @param frameId      frameLayout id
     * @param showFragment 需要显示的 Fragment
     */
    public void replaceFragment(@IdRes int frameId, Fragment showFragment) {
        Fragment currentFragment = fragmentArray.get(frameId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isCloseSelf) {
            finish();
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
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 生命周期相关
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null)
            mPresenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null)
            mPresenter.onStop();
    }

    /**
     * 判断当前线程是否在主线程
     */
    public boolean isUIThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    /**
     * 沉浸式状态栏
     */
    public void translucentStatus() {
        //5.0及以上系统才支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.L) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 隐藏状态栏
     */
    public void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Android 4.4及以上系统才支持
        if (statusMode == FULL_SCREEN_STATUS && hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 弱引用 handler
     */
    public static class WeakHandler extends Handler {
        WeakReference weakReference;

        WeakHandler(BaseMvpActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseMvpActivity activity = (BaseMvpActivity) weakReference.get();
            switch (msg.what) {
                default:
                    if (activity != null) {
                        activity.handleMessage(msg);
                    }
                    break;
            }
        }
    }

}