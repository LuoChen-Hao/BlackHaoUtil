package com.balckhao.blackhaoutil.mvp.test;

import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;
import com.balckhao.blackhaoutil.mvp.BaseMvpActivity;
import com.balckhao.blackhaoutil.mvp.BaseMvpFragment;

/**
 * Author ： BlackHao
 * Time : 2018/12/20 10:15
 * Description :
 */
public class MvpTestFragment extends BaseMvpFragment<TestMvpContract.TestPresenterImpl>
        implements TestMvpContract.TestViewImpl, View.OnClickListener {

    private EditText etName;
    private EditText etPsw;
    private TextView tvLoginResult;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_mvp_test;
    }

    @Override
    protected void initUI(View view) {
        etName = (EditText) view.findViewById(R.id.et_name);
        etPsw = (EditText) view.findViewById(R.id.et_psw);
        tvLoginResult = (TextView) view.findViewById(R.id.tv_login_result);
        view.findViewById(R.id.bt_login).setOnClickListener(this);
        view.setBackgroundColor(Color.parseColor("#ebebeb"));
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected TestMvpContract.TestPresenterImpl initPresenter() {
        return new TestPresenter(getContext(), this);
    }

    @Override
    public void loginResult(String txt) {
        tvLoginResult.setText(txt);
    }

    @Override
    public void onClick(View view) {
        getPresenter().login(etName.getText().toString(), etPsw.getText().toString());
    }
}
