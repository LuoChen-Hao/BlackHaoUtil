package com.balckhao.blackhaoutil.imgProxy;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.balckhao.blackhaoutil.R;
import com.blackhao.utillibrary.imgProxy.ImgProxyImpl;
import com.blackhao.utillibrary.regular.RegularUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;

/**
 * Author ： BlackHao
 * Time : 2019/5/8 14:26
 * Description :
 */
public class ImgLoaderUtil implements ImgProxyImpl {

    private ImageLoader imageLoader;

    public ImgLoaderUtil(Context context) {
        imageLoader = ImageLoader.getInstance();
        init(context);
    }

    private static void init(Context context) {
        //
        DisplayImageOptions optionsDefault = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.mipmap.ic_launcher_round)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher_round)
                .build();

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;// 使用最大可用内存值的1/4作为缓存的大小。

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.threadPriority(Thread.NORM_PRIORITY - 1);// 设置线程的优先级【10， 1】，高优先级 -> 低优先级.
        builder.threadPoolSize(3);
        builder.denyCacheImageMultipleSizesInMemory();// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
        builder.memoryCacheSize(cacheSize);
        builder.defaultDisplayImageOptions(optionsDefault);
        builder.imageDownloader(new BaseImageDownloader(context, 5000, 20000));

        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);

        L.disableLogging();
    }

    @Override
    public void displayAvatar(ImageView iv, String url) {
        if (!RegularUtil.isURL(url)) {
            url = "file://" + url;
        }
        imageLoader.displayImage(url, iv);
    }

    @Override
    public void displayAvatar(ImageView iv, String url, int loadErrorResId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(loadErrorResId)
                .showImageOnLoading(loadErrorResId)
                .showImageForEmptyUri(loadErrorResId)
                .build();
        if (!RegularUtil.isURL(url)) {
            url = "file://" + url;
        }
        imageLoader.displayImage(url, iv, options);
    }

    @Override
    public void displayAvatar(ImageView iv, int resId) {
        String url = "drawable://" + resId;
        imageLoader.displayImage(url, iv);
    }

    @Override
    public void displayImg(ImageView iv, String url) {
        if (!RegularUtil.isURL(url)) {
            url = "file://" + url;
        }
        imageLoader.displayImage(url, iv);
    }

    @Override
    public void displayImg(ImageView iv, int resId) {
        String url = "drawable://" + resId;
        imageLoader.displayImage(url, iv);
    }

    @Override
    public void displayImg(ImageView iv, String url, int loadErrorResId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(loadErrorResId)
                .showImageOnLoading(loadErrorResId)
                .showImageForEmptyUri(loadErrorResId)
                .build();
        if (!RegularUtil.isURL(url)) {
            url = "file://" + url;
        }
        imageLoader.displayImage(url, iv, options);
    }
}
