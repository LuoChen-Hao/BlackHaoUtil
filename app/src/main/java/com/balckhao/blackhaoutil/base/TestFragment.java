package com.balckhao.blackhaoutil.base;

import android.view.View;
import android.widget.ImageView;

import com.balckhao.blackhaoutil.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author ： BlackHao
 * Time : 2018/8/1 10:42
 * Description :
 */
public class TestFragment extends BaseFragment {


    @Bind(R.id.iv_1)
    ImageView iv1;

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_one;
    }

    @Override
    protected void initUI(View view) {
        ButterKnife.bind(this, view);
        iv1.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
