package com.tangramdemo;

import android.animation.ArgbEvaluator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;

import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.tangramdemo.pageindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class TablayoutActivity extends AppCompatActivity {

    TabLayout mytab;
    private ViewPager mViewPager;

    List<String> mTitle;
    List<Fragment> mFragment;
    Fragment1 tab1;
    Fragment1 tab2;
    Fragment1 tab3;
    Fragment1 tab4,tab5;
    Fragment1 tab;
    private FrameLayout framelayout;
    private CirclePageIndicator host_indicator_dot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);

        mytab = findViewById(R.id.mytab);
        mViewPager = findViewById(R.id.mViewPager);
        framelayout = findViewById(R.id.framelayout);
        host_indicator_dot = findViewById(R.id.host_indicator_dot);
        mViewPager.setOffscreenPageLimit(2);

        mytab.addTab(mytab.newTab().setText("选项卡一").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡二").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡三").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡四").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡五").setIcon(R.mipmap.ic_launcher));

        mTitle = new ArrayList<>();
        mTitle.add("选项卡一");
        mTitle.add("选项卡二");
        mTitle.add("选项卡三");
        mTitle.add("选项卡四");
        mTitle.add("选项卡五");

        tab1 = new Fragment1();
        tab2 = new Fragment1();
        tab3 = new Fragment1();
        tab4 = new Fragment1();
        tab5 = new Fragment1();
        mFragment = new ArrayList<>();
        mFragment.add(tab1);
        mFragment.add(tab2);
        mFragment.add(tab3);
        mFragment.add(tab4);
        mFragment.add(tab5);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mFragment.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitle.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        mytab.setupWithViewPager(mViewPager);
        host_indicator_dot.setCircle(true);
        host_indicator_dot.setBackgroundResource(R.drawable.host_focus_indicator);
        host_indicator_dot.setViewPager(mViewPager);
        addPageChangeListener();

    }


    private int lastValue = -1;
    private boolean isLeft = true;
    private int currentPositon, nextPosition;
    private boolean isSliding = false;

    private void addPageChangeListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                if (positionOffset != 0.0f) {
                    isSliding = true;
                    //左右滑只ViewPager的滑动
                    //滑动过程
                    if (lastValue > positionOffsetPixels) {
                        //右滑
                        isLeft = false;
                        currentPositon = position + 1;
                        nextPosition = position;
                    } else if (positionOffsetPixels > lastValue) {
                        //左滑
                        isLeft = true;
                        currentPositon = position;
                        nextPosition = position + 1;
                    }
                } else {
                    isSliding = false;
                    currentPositon = position;
                    nextPosition = position;
                    //不滑动过程 第一次进入 或滑动结束 或边界不能滑
                }

                lastValue = positionOffsetPixels;

                if (isLeft) {
                    setBgColor(isSliding, currentPositon, nextPosition, positionOffset);
                } else {
                    setBgColor(isSliding, currentPositon, nextPosition, 1 - positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Bitmap currentBitmap = null;
    private Bitmap nextBitmap = null;
    private int currentColor, nextColor;

    public void setBgColor(boolean isSliding, int currentPositon, int nextPosition, float positionOffset) {
        if (positionOffset > 1.0f) return;

        currentBitmap = getBitmap(currentPositon);
        if (currentBitmap != null) {
            currentColor = BitmapUtil.getMainColor(currentBitmap);
        }

//        int color = currentColor;
        if (isSliding) {
            //需要渐变
            nextBitmap = getBitmap(nextPosition);
            if (nextBitmap != null) {
                nextColor = BitmapUtil.getMainColor(nextBitmap);
            }else {
                nextColor = ContextCompat.getColor(TablayoutActivity.this,R.color.white);
            }

            ArgbEvaluator argbEvaluator = new ArgbEvaluator();//渐变色计算类
            currentColor = (int) (argbEvaluator.evaluate(positionOffset, currentColor, nextColor));
        }

        mytab.setBackgroundColor(currentColor);
        framelayout.setBackgroundColor(currentColor);
    }

    public void setGraColor(float positionOffset,int endColor){
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();//渐变色计算类
        int color = (int) (argbEvaluator.evaluate(positionOffset, currentColor, endColor));
        mytab.setBackgroundColor(color);
        framelayout.setBackgroundColor(color);
    }

    private Bitmap getBitmap(int position) {
        if (position == 0) {
            tab = tab1;
        } else if (position == 1) {
            tab = tab2;
        } else if (position == 2) {
            tab = tab3;
        } else if (position == 3) {
            tab = tab4;
        }else if (position == 4){
            tab = tab5;
        }
        return tab.getBitmap(position);
    }

}
