package com.balckhao.blackhaoutil.imgProxy;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.balckhao.blackhaoutil.R;
import com.blackhao.utillibrary.imgProxy.ImgDisplayUtil;
import com.blackhao.utillibrary.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2019/5/8 14:40
 * Description :
 */
public class ListActivity extends BaseActivity {

    private ListView lv_test;
    private String[] avatars = new String[]{"http://192.168.10.189:8700/g0/000/000/icon_1553508473399.png",
            "http://192.168.11.41:8700/g0/000/000/e601e688d17142f3ab432fe1afa4f68b.jpg",
            "http://192.168.10.189:8700/g0/000/000/icon_1555063164902.png", "", ""};
    private String[] imgs = new String[]{"http://192.168.10.189:8700/g0/000/000/7aaaa19a198fa5d5ae533b238e5d3c22.jpg",
            "http://192.168.10.189:8700/g0/000/000/b03f0e27fda43db7fcee73333c0a6598.png",
            "http://192.168.10.189:8700/g0/000/000/1515f3e4155d63e5a1b97595de821f9a.png", "", ""};
    //
    private List<String> list = new ArrayList<>();


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_list);
        lv_test = findViewById(R.id.lv_test);
        ImgDisplayUtil.getInstance().init(new ImgLoaderUtil(this));
    }

    @Override
    protected void initData() {
        ImgAdapter adapter = new ImgAdapter();
        list.add("11111111111");
        list.add("22222222222");
        list.add("333333333333");
        list.add("4444444444444");
        list.add("555555555555555");
        list.add("333333333333");
        list.add("4444444444444");
        list.add("555555555555555");
        list.add("333333333333");
        list.add("4444444444444");
        list.add("555555555555555");
        lv_test.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void handleMessage(Message msg) {

    }

    private class ImgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.adapter_img, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImgDisplayUtil.getInstance().displayAvatar(holder.iv1, avatars[position % 5], R.drawable.doc);
            ImgDisplayUtil.getInstance().displayImg(holder.iv2, imgs[position % 5], R.drawable.image);
            return convertView;
        }

        private class ViewHolder {
            ImageView iv1;
            ImageView iv2;

            ViewHolder(View v) {
                this.iv1 = v.findViewById(R.id.iv_1);
                this.iv2 = v.findViewById(R.id.iv_2);
            }
        }
    }
}
