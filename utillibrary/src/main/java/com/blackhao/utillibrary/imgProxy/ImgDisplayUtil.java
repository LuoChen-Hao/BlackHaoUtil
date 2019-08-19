package com.blackhao.utillibrary.imgProxy;

import android.widget.ImageView;

import com.blackhao.utillibrary.log.LogHelper;

/**
 * Author ： BlackHao
 * Time : 2019/5/8 13:49
 * Description : 图片显示代理
 */
public class ImgDisplayUtil implements ImgProxyImpl {

    private ImgProxyImpl impl;
    //单例模式
    private static ImgDisplayUtil util;
    //Log 打印
    private LogHelper log = LogHelper.getInstance();

    public synchronized static ImgDisplayUtil getInstance() {
        if (util == null) {
            util = new ImgDisplayUtil();
        }
        return util;
    }

    /**
     * 初始化
     */
    public void init(ImgProxyImpl impl) {
        this.impl = impl;
    }

    @Override
    public void displayAvatar(ImageView iv, String url) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayAvatar(iv, url);
    }

    @Override
    public void displayAvatar(ImageView iv, String url, int loadErrorResId) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayAvatar(iv, url, loadErrorResId);
    }

    @Override
    public void displayAvatar(ImageView iv, int resId) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayAvatar(iv, resId);
    }

    @Override
    public void displayImg(ImageView iv, String url) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayImg(iv, url);
    }

    @Override
    public void displayImg(ImageView iv, int resId) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayImg(iv, resId);
    }

    @Override
    public void displayImg(ImageView iv, String url, int loadErrorResId) {
        if (impl == null) {
            log.e("ImgDisplayUtil Not Init");
            return;
        }
        impl.displayImg(iv, url, loadErrorResId);
    }
}
