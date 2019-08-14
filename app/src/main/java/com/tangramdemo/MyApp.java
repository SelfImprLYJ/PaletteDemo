package com.tangramdemo;

import android.app.Application;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * @author liyongjian
 * @date 2019-08-08.
 * Description：
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                Glide.with(MyApp.this).load(url).into(view);
            }
        }, ImageView.class);
    }
}
