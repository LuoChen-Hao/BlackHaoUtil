package com.balckhao.blackhaoutil.base;

import android.annotation.StringRes;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.balckhao.blackhaoutil.logUtil.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Author ： 章浩
 * Time : 2018/6/20 16:38
 * Description : 教育一体机 BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    //Handler
    public WeakHandler handler;
    //Toast 间隔时间
    private static long TOAST_INTERVALS = 1000;
    //Toast上次显示时间（避免短时间内多次调用）
    private long showToastTime = 0;
    //用于保存对应 FrameLayout当前显示的 Fragment
    private SparseArray<Fragment> fragmentArray;
    //LogUtil
    protected LogUtil log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //初始化Log
        log = LogUtil.getInstance();
        //初始化fragmentArray
        fragmentArray = new SparseArray<>();
        //初始化UI
        initUI();
        //view 初始化完成
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
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
     * 初始化 Handler（不用 handler 时,不需要调用该方法）
     */
    public void initHandler() {
        handler = new WeakHandler(this);
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    public void showToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //避免多次调用
                if (System.currentTimeMillis() - showToastTime > TOAST_INTERVALS) {
                    Toast.makeText(BaseActivity.this, str, Toast.LENGTH_SHORT).show();
                    //更新显示时间
                    showToastTime = System.currentTimeMillis();
                }
            }
        });
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    public void showToast(@StringRes int strId) {
        showToast(getString(strId));
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
     * 弱引用 handler
     */
    public static class WeakHandler extends Handler {
        WeakReference weakReference;

        WeakHandler(BaseActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity activity = (BaseActivity) weakReference.get();
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
