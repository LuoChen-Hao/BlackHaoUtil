package com.balckhao.blackhaoutil.mvp.test;

import android.content.Context;

import com.blackhao.utillibrary.mvp.BaseMvpPresenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Author ： BlackHao
 * Time : 2018/12/20 10:17
 * Description : TestPresenter
 */
public class TestPresenter extends BaseMvpPresenter<TestMvpContract.TestViewImpl>
        implements TestMvpContract.TestPresenterImpl {

    public TestPresenter(Context context, TestMvpContract.TestViewImpl mView) {
        super(context, mView);
    }

    @Override
    public void login(final String name, final String psw) {
        getView().showLoading("正在登陆...");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //休眠，模拟登陆
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (name.equals("admin") && psw.equals("admin")) {
                    getView().loginResult("登陆成功，Login ID:" + 123456);
                } else {
                    getView().loginResult("登陆失败，用户不存在");
                    getView().showToast("登陆失败，用户不存在");
                }
                getView().dismissLoading();
            }
        });
    }

    @Override
    public void loginOut() {

    }
}
