package com.balckhao.blackhaoutil.base;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;

import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2018/9/4 14:58
 * Description :
 */
public class TestAdapter extends CommonBaseAdapter<String> {

    public TestAdapter(List<String> list, Context context) {
        super(list, context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.adapter_test;
    }

    @Override
    protected int[] bindView() {
        return new int[]{R.id.test_iv, R.id.test_tv};
    }

    @Override
    protected void initData(ViewHolder holder, String s, int position) {
        TextView tv = holder.getViewById(R.id.test_tv);
        ImageView iv = holder.getViewById(R.id.test_iv);
        tv.setText("Position:" + position + "  Content : " + s);
        if (s.endsWith("pdf")) {
            iv.setBackgroundResource(R.drawable.pdf);
        } else if (s.endsWith("ppt")){
            iv.setBackgroundResource(R.drawable.ppt);
        }else {
            iv.setBackgroundResource(R.drawable.unkown_file);
        }
    }
}
