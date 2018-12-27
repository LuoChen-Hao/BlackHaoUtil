package com.balckhao.blackhaoutil.mvp.test;


import com.blackhao.utillibrary.mvp.BaseMvpPresenterImpl;
import com.blackhao.utillibrary.mvp.BaseMvpViewImpl;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 10:16
 * Description :
 */
public class TestMvpContract {

    public interface TestViewImpl extends BaseMvpViewImpl {

        void loginResult(String txt);

    }

    public interface TestPresenterImpl extends BaseMvpPresenterImpl {

        void login(String name ,String psw);

        void loginOut();
    }
}
