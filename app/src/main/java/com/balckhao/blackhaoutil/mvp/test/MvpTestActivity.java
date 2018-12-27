package com.balckhao.blackhaoutil.mvp.test;

import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;
import com.blackhao.utillibrary.mvp.BaseMvpActivity;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 10:15
 * Description :
 */
public class MvpTestActivity extends BaseMvpActivity<TestMvpContract.TestPresenterImpl>
        implements TestMvpContract.TestViewImpl {

    private EditText etName;
    private EditText etPsw;
    private TextView tvLoginResult;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_mvp_test);
        etName = (EditText) findViewById(R.id.et_name);
        etPsw = (EditText) findViewById(R.id.et_psw);
        tvLoginResult = (TextView) findViewById(R.id.tv_login_result);
        replaceFragment(R.id.fl_test, new MvpTestFragment());
    }

    @Override
    public int getStatusMode() {
        return TRANSLUCENT_STATUS;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void handleMessage(Message msg) {
        log.e(msg.what + "");
    }

    @Override
    protected TestMvpContract.TestPresenterImpl initPresenter() {
        return new TestPresenter(this, this);
    }

    @Override
    public void loginResult(String txt) {
        tvLoginResult.setText(txt);
        handler.sendEmptyMessage(0);
    }

    public void onClick(View view) {
        getPresenter().login(etName.getText().toString(), etPsw.getText().toString());
    }
}
