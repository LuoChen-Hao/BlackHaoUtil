package com.balckhao.blackhaoutil.base.test;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.balckhao.blackhaoutil.R;
import com.blackhao.utillibrary.base.BaseFragment;

import java.util.ArrayList;

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
    @Bind(R.id.lv_test)
    ListView lvTest;

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
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Hello World, Android.ppt");
        arrayList.add("Hello World, Java.pdf");
        arrayList.add("Hello World, JS.pdf");
        arrayList.add("Hello World, C.ppt");
        arrayList.add("Hello World, C++");
        arrayList.add("Hello World, C#.pdf");
        arrayList.add("Hello World, Python.ppt");
        arrayList.add("Hello World, PHP");
        TestAdapter testAdapter = new TestAdapter(arrayList, getContext());
        lvTest.setAdapter(testAdapter);
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
