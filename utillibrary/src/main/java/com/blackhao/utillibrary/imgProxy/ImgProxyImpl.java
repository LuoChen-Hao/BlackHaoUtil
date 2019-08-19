package com.blackhao.utillibrary.imgProxy;

import android.widget.ImageView;

/**
 * Author ： BlackHao
 * Time : 2019/5/8 13:49
 * Description : 图像显示代理回调
 */
public interface ImgProxyImpl {
    /**
     * 显示头像
     *
     * @param iv  ImageView
     * @param url 图片路径
     */
    void displayAvatar(ImageView iv, String url);

    /**
     * 显示头像
     *
     * @param iv           ImageView
     * @param url          图片路径
     * @param loadErrorResId 加载失败默认资源 id
     */
    void displayAvatar(ImageView iv, String url, int loadErrorResId);

    /**
     * 显示头像
     *
     * @param iv    ImageView
     * @param resId 图片资源 id
     */
    void displayAvatar(ImageView iv, int resId);

    /**
     * 显示图片
     *
     * @param iv  ImageView
     * @param url 图片路径
     */
    void displayImg(ImageView iv, String url);

    /**
     * 显示图片
     *
     * @param iv    ImageView
     * @param resId 图片资源 id
     */
    void displayImg(ImageView iv, int resId);

    /**
     * 显示头像
     *
     * @param iv           ImageView
     * @param url          图片路径
     * @param loadErrorResId 加载失败资源 id
     */
    void displayImg(ImageView iv, String url, int loadErrorResId);

}
