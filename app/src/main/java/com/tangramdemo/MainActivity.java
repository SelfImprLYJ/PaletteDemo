package com.tangramdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libra.Utils;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.tmall.wireless.tangram.support.SimpleClickSupport;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TangramEngine engine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_view);
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(MainActivity.this);

//        builder.registerCell("type", TestView.class);

        engine = builder.build();
//        engine.register(SimpleClickSupport.class, new XXClickSupport());
//        engine.register(CardLoadSupport.class, new XXCardLoadSupport());
//        engine.register(ExposureSupport.class, new XXExposureSuport());
        engine.bindView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                engine.onScrolled();
            }
        });

        //预加载量默认5个
        engine.setPreLoadNumber(3);

        byte[] bytes = getAssertsFile(this, "data.json");
        if (bytes != null) {
            String json = new String(bytes);
            try {
                JSONArray data = new JSONArray(json);
                engine.setData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.destroy();
//        通过主动调用 destroy 方法，可以释放内部的资源，比如清理 adapter、清理事件总线缓存的未处理消息、注销广播等。
//        注意调用 destroy 方法之后就不需要调用 unbind 方法了。
    }
}
