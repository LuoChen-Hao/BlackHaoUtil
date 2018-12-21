package com.balckhao.blackhaoutil.base.test;

import android.os.Message;
import android.widget.TextView;

import com.balckhao.blackhaoutil.R;
import com.balckhao.blackhaoutil.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author ： BlackHao
 * Time : 2018/8/1 10:35
 * Description : BaseActivity LogUtil Test
 */
public class TestActivity extends BaseActivity {
    @Bind(R.id.tv_show_time)
    TextView tvShowTime;

    private String testJson1 = "{\"UserID\":01, \"Name\":\"Test\", \"Email\":\"test@gmail.com\"};";

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_base_test);
        ButterKnife.bind(this);
        //replaceFragment
        replaceFragment(R.id.frame_1, new TestFragment());
        replaceFragment(R.id.frame_2, new TestFragment());
    }

    @Override
    protected void initData() {
        //初始化Handler
        initHandler();
        handler.sendEmptyMessageDelayed(0, 1000);
        log.json(testJson1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2048; i++) {
            sb.append(i).append(" ");
            if (i + 1 % 100 == 0) {
                sb.append("\n");
            }
        }
        log.e(sb.toString());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String time = df.format(new Date());
                tvShowTime.setText(time);
                handler.sendEmptyMessageDelayed(0, 1000);
                break;
        }
    }

}
