package com.tangramdemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

/**
 * @author liyongjian
 * @date 2019-08-09.
 * Description：
 */
public class BitmapUtil {

    private static final int QUANTIZE_WORD_WIDTH = 5;
    private static final int QUANTIZE_WORD_MASK = (1 << QUANTIZE_WORD_WIDTH) - 1;

    public   static int getMainColor(Bitmap bitmap) {
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

    private static int quantizeFromRgb888(int color) {
        int r = modifyWordWidth(Color.red(color), 8, QUANTIZE_WORD_WIDTH);
        int g = modifyWordWidth(Color.green(color), 8, QUANTIZE_WORD_WIDTH);
        int b = modifyWordWidth(Color.blue(color), 8, QUANTIZE_WORD_WIDTH);
        return r << (QUANTIZE_WORD_WIDTH + QUANTIZE_WORD_WIDTH) | g << QUANTIZE_WORD_WIDTH | b;
    }

    private static int approximateToRgb888(int color) {
        return approximateToRgb888(quantizedRed(color), quantizedGreen(color), quantizedBlue(color));
    }

   static int quantizedRed(int color) {
        return (color >> (QUANTIZE_WORD_WIDTH + QUANTIZE_WORD_WIDTH)) & QUANTIZE_WORD_MASK;
    }

   static int quantizedGreen(int color) {
        return (color >> QUANTIZE_WORD_WIDTH) & QUANTIZE_WORD_MASK;
    }

   static int quantizedBlue(int color) {
        return color & QUANTIZE_WORD_MASK;
    }
    static int  approximateToRgb888(int r, int g, int b) {
        return Color.rgb(modifyWordWidth(r, QUANTIZE_WORD_WIDTH, 8),
                modifyWordWidth(g, QUANTIZE_WORD_WIDTH, 8),
                modifyWordWidth(b, QUANTIZE_WORD_WIDTH, 8));
    }

    private static int modifyWordWidth(int value, int currentWidth, int targetWidth) {
        final int newValue;
        if (targetWidth > currentWidth) {
            newValue = value << (targetWidth - currentWidth);
        } else {
            newValue = value >> (currentWidth - targetWidth);
        }
        return newValue & ((1 << targetWidth) - 1);
    }

    private static boolean shouldIgnoreColor(int color565 ,float[] tempHsl) {
        final int rgb = approximateToRgb888(color565);
        ColorUtils.colorToHSL(rgb, tempHsl);
        return !isAllowed(rgb, tempHsl);
    }

    public static boolean isAllowed(int rgb, float[] hsl) {
        return !isWhite(hsl) && !isBlack(hsl);
    }

    private static boolean isBlack(float[] hslColor) {
        return hslColor[2] <= 0.01F;
    }

    private static boolean isWhite(float[] hslColor) {
        return hslColor[2] >= 0.95F;
    }
}
