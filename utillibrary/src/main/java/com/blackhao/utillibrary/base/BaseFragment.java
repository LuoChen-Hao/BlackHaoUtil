package com.blackhao.utillibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackhao.utillibrary.logUtil.LogUtil;

/**
 * Author ： BlackHao
 * Time : 2018/6/20 14:55
 * Description : 教育一体机 BaseFragment
 */

public abstract class BaseFragment extends Fragment {

    //对应的activity
    protected BaseActivity baseActivity;
    //用于保存对应FrameLayout当前显示的Fragment
    private SparseArray<Fragment> fragmentArray;
    //LogUtil
    protected LogUtil log;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        fragmentArray = new SparseArray<>();
        log = baseActivity.log;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initLayoutRes(), container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }

    /**
     * 用于返回布局文件ID
     */
    protected abstract int initLayoutRes();

    /**
     * UI初始化
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
     * 显示 Toast（可以在子线程中调用）
     */
    public void showToast(String str) {
        baseActivity.showToast(str);
    }

    /**
     * 显示 Toast（可以在子线程中调用）
     */
    public void showToast(@StringRes int strId) {
        baseActivity.showToast(strId);
    }

    /**
     * 替换fragment
     *
     * @param frameId      frameLayout id
     * @param showFragment 需要显示的 Fragment
     */
    public void replaceFragment(int frameId, Fragment showFragment) {
        Fragment currentFragment = fragmentArray.get(frameId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //用于fragment的显示与隐藏
        if (currentFragment != null && currentFragment != showFragment) {
            // 先判断是否被add过
            if (!showFragment.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(currentFragment).add(frameId, showFragment).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(currentFragment).show(showFragment).commit();
            }
            fragmentArray.put(frameId, showFragment);
        } else if (currentFragment == null) {
            // 先判断是否被add过
            if (!showFragment.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(frameId, showFragment).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.show(showFragment).commit();
            }
            fragmentArray.put(frameId, showFragment);
        }
    }

    /**
     * activity 跳转
     *
     * @param target      需要跳转的Activity
     * @param bundle      bundle
     * @param isCloseSelf 是否关闭当前的activity
     */
    public void openActivity(Class<?> target, Bundle bundle, boolean isCloseSelf) {
        baseActivity.openActivity(target, bundle, isCloseSelf);
    }

    /**
     * activity 跳转（不需要传值）
     *
     * @param target 需要跳转的 Activity
     */
    public void openActivity(Class<?> target) {
        openActivity(target, null, false);
    }

}
