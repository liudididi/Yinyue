package com.liu.asus.yinyue;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by 地地 on 2017/11/10.
 * 邮箱：461211527@qq.com.
 */

public class MyApplictions  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions option=new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .build();
        ImageLoaderConfiguration con=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(option)
                .build();
        ImageLoader.getInstance().init(con);


    }
}
