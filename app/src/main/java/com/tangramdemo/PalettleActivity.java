package com.tangramdemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


public class PalettleActivity extends AppCompatActivity {


    private ImageView image;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;

    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palettle);
        image = findViewById(R.id.image);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);

//        Glide.with(this).load(R.mipmap.ic_launcher).into(image);
        Glide.with(PalettleActivity.this).load("http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565781371978&di=bb33016e5b3749aa5c1342a6c04d9d55&imgtype=0&src=http%3A%2F%2Fimg3.redocn.com%2Ftupian%2F20121013%2Fshouhuihongweijinbaitu_1436109_small.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                image.setImageBitmap(resource);
                mBitmap = resource;
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // 获取到柔和的深色的颜色（可传默认值）
                        int darkMutedColor = palette.getDarkMutedColor(Color.BLUE);
                        tv1.setBackgroundColor(darkMutedColor);
                        // 获取到活跃的深色的颜色（可传默认值）
                        int darkVibrantColor = palette.getDarkVibrantColor(Color.BLUE);
                        tv2.setBackgroundColor(darkVibrantColor);
                        // 获取到柔和的明亮的颜色（可传默认值）
                        int lightMutedColor = palette.getLightMutedColor(Color.BLUE);
                        tv3.setBackgroundColor(lightMutedColor);
                        // 获取到活跃的明亮的颜色（可传默认值）
                        int lightVibrantColor = palette.getLightVibrantColor(Color.BLUE);
                        tv4.setBackgroundColor(lightVibrantColor);
                        // 获取图片中最活跃的颜色（也可以说整个图片出现最多的颜色）（可传默认值）
                        int vibrantColor = palette.getVibrantColor(Color.BLUE);
                        tv5.setBackgroundColor(vibrantColor);
                        // 获取图片中一个最柔和的颜色（可传默认值）
                        int mutedColor = palette.getMutedColor(Color.BLUE);
                        tv6.setBackgroundColor(mutedColor);


                        tv7.setBackgroundColor(getMainColor(mBitmap));

                    }
                });
            }
        });
    }

    private static final int QUANTIZE_WORD_WIDTH = 5;
    private static final int QUANTIZE_WORD_MASK = (1 << QUANTIZE_WORD_WIDTH) - 1;

    private  int getMainColor(Bitmap bitmap) {
        int v = (int) (bitmap.getWidth() * 0.020);
        int[] pixels = new int[v*v];
        bitmap.getPixels(pixels ,0 ,v ,0 ,0 ,v ,v);

        final int[] hist = new int[1 << (QUANTIZE_WORD_WIDTH * 3)];
        for (int pixel : pixels) {
            final int quantizedColor = quantizeFromRgb888(pixel);
            hist[quantizedColor]++;
        }

        int maxSize = 0;
        int maxCountColor = Integer.MIN_VALUE;
        float[] mTempHsl = new float[3];
        for (int color = 0; color < hist.length; color++) {
            int i1 = hist[color];
            if(i1 > 0 && shouldIgnoreColor(color ,mTempHsl)) {
                hist[color] = 0;
                continue;
            }

            if (i1 > 0 && maxSize < i1) {
                maxSize = i1;
                maxCountColor = color;
            }
        }

        if(maxCountColor == Integer.MIN_VALUE) {
            return Color.TRANSPARENT;
        }

        return approximateToRgb888(maxCountColor);
    }

    private  int quantizeFromRgb888(int color) {
        int r = modifyWordWidth(Color.red(color), 8, QUANTIZE_WORD_WIDTH);
        int g = modifyWordWidth(Color.green(color), 8, QUANTIZE_WORD_WIDTH);
        int b = modifyWordWidth(Color.blue(color), 8, QUANTIZE_WORD_WIDTH);
        return r << (QUANTIZE_WORD_WIDTH + QUANTIZE_WORD_WIDTH) | g << QUANTIZE_WORD_WIDTH | b;
    }

    private  int approximateToRgb888(int color) {
        return approximateToRgb888(quantizedRed(color), quantizedGreen(color), quantizedBlue(color));
    }

     int quantizedRed(int color) {
        return (color >> (QUANTIZE_WORD_WIDTH + QUANTIZE_WORD_WIDTH)) & QUANTIZE_WORD_MASK;
    }

     int quantizedGreen(int color) {
        return (color >> QUANTIZE_WORD_WIDTH) & QUANTIZE_WORD_MASK;
    }

     int quantizedBlue(int color) {
        return color & QUANTIZE_WORD_MASK;
    }
     int approximateToRgb888(int r, int g, int b) {
        return Color.rgb(modifyWordWidth(r, QUANTIZE_WORD_WIDTH, 8),
                modifyWordWidth(g, QUANTIZE_WORD_WIDTH, 8),
                modifyWordWidth(b, QUANTIZE_WORD_WIDTH, 8));
    }

    private  int modifyWordWidth(int value, int currentWidth, int targetWidth) {
        final int newValue;
        if (targetWidth > currentWidth) {
            newValue = value << (targetWidth - currentWidth);
        } else {
            newValue = value >> (currentWidth - targetWidth);
        }
        return newValue & ((1 << targetWidth) - 1);
    }

    private  boolean shouldIgnoreColor(int color565 ,float[] tempHsl) {
        final int rgb = approximateToRgb888(color565);
        ColorUtils.colorToHSL(rgb, tempHsl);
        return !isAllowed(rgb, tempHsl);
    }

    public  boolean isAllowed(int rgb, float[] hsl) {
        return !isWhite(hsl) && !isBlack(hsl);
    }

    private  boolean isBlack(float[] hslColor) {
        return hslColor[2] <= 0.01F;
    }

    private  boolean isWhite(float[] hslColor) {
        return hslColor[2] >= 0.95F;
    }
}
