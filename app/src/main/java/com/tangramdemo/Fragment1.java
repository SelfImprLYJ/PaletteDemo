package com.tangramdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liyongjian
 * @date 2019-08-09.
 * Description：
 */
public class Fragment1 extends Fragment {


    private int position = 0;
    private String mUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2959631642,3930733773&fm=26&gp=0.jpg";
    private RecyclerView rv;

      private List<String> iconList = new ArrayList<>();
    private int mDistance,maxDistance = 500;
    private float alpha = 0.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position");
        }
        View inflate = LayoutInflater.from(Fragment1.this.getActivity()).inflate(R.layout.layout_fragment1, null);
        rv = inflate.findViewById(R.id.rv);
        init();

        //创建一个layoutManager，这里使用LinearLayoutManager指定为线性，也就可以有ListView这样的效果
        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment1.this.getActivity());
        //完成layoutManager设置
        rv.setLayoutManager(layoutManager);
        //创建IconAdapter的实例同时将iconList传入其构造函数
        IconAdapter adapter = new IconAdapter(iconList,Fragment1.this.getActivity(),position);
        //完成adapter设置
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDistance += dy;
                alpha = mDistance * 1.0f / maxDistance;//百分比
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                }
                if (alpha <= 0.0f) {
                    alpha = 0.0f;
                }

                TablayoutActivity activity = (TablayoutActivity) getActivity();
                if (activity != null) {
                    activity.setGraColor(alpha, ContextCompat.getColor(Fragment1.this.getActivity(),R.color.white));
                }
                Log.d("onScrolled","onScrolled: " + alpha);
            }
        });

        adapter.setOnGlideListener(new IconAdapter.OnGlideListener() {
            @Override
            public void loadSuccess(Bitmap bitmap) {
                mBitmap = bitmap;

                if (!hasLoadBitmap && noBitmapPosition == position){
                    TablayoutActivity activity = (TablayoutActivity) getActivity();
                    if (activity != null) {
                        activity.setBgColor(false,position,position,0.0f);
                    }
                }
            }
        });

        return inflate;
    }

    private void init() {
        for (int i=0;i<50;i++) {
            iconList.add("data");
        }
    }

    private Bitmap mBitmap;
    private boolean hasLoadBitmap;
    private int noBitmapPosition;

    public Bitmap getBitmap(int position) {
        hasLoadBitmap = true;
        if (mBitmap == null) {
            noBitmapPosition = position;
            hasLoadBitmap = false;
        }
        return mBitmap;
    }
}
