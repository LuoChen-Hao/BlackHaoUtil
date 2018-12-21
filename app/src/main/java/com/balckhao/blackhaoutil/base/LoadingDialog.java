package com.balckhao.blackhaoutil.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;

/**
 * Author ： BlackHao
 * Time : 2018/12/4 16:30
 * Description : 加载框
 */
public class LoadingDialog extends DialogFragment {

    private String loadingTxt = "";
    private TextView tvMsg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_loading, container, false);
        tvMsg = (TextView) view.findViewById(R.id.login_status_message);
        if (!loadingTxt.equals("")) {
            tvMsg.setText(loadingTxt);
        }
        //设置动画
        RotateAnimation animation = new RotateAnimation(0, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        view.findViewById(R.id.loading_img).startAnimation(animation);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
    }

    public void setLoadingTxt(String loadingTxt) {
        this.loadingTxt = loadingTxt;
        if (this.tvMsg != null) {
            this.tvMsg.setText(loadingTxt);
        }
    }

    public void setLoadingTxt(@StringRes int strId) {
        setLoadingTxt(getString(strId));
    }
}
