package com.musk.lib.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片缩放工具类
 */
public class DrawableUtils {

    // 放大缩小图片
    public static Bitmap scaleTo(final Bitmap bitmapOrg, final int newWidth, final int newHeight) {
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        if (width == 0 || height == 0) {
            return bitmapOrg;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
    }

    // 放大缩小图片
    public static Bitmap scaleTo(final Bitmap bitmapOrg, final float scaleWidth, final float scaleHeight) {
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
    }

    public static Bitmap byte2Bitmap(Context mContext, byte[] b) {
        if (b != null) {
            if (b.length != 0) {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            } else {
                return BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.sym_def_app_icon);
            }
        }
        return BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.sym_def_app_icon);
    }

    public static Bitmap drawable2Bitmap(Context mContext, Drawable drawable) {
        if (drawable instanceof ColorDrawable) {
            return null;
        }
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static Bitmap getBitmap(Context mContext, int resId) {
        return BitmapFactory.decodeResource(mContext.getResources(), resId);
    }

    public static Bitmap getBitmap(String file) {
        if (new File(file).exists()) {
            return BitmapFactory.decodeFile(file);
        }
        return null;
    }

    public static Bitmap getBitmap(Context mContext, String file, Options options) {
        if (new File(file).exists()) {
            return BitmapFactory.decodeFile(file, options);
        }
        return null;
    }

    public static Bitmap getBitmap(Context mContext, InputStream inputStream) {
        if (inputStream != null) {
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }

    public static byte[] drawable2Byte(Drawable icon) {
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] bitmap2Byte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Drawable bitmap2Drawable(Context mContext, Bitmap bitmap) {
        return new BitmapDrawable(mContext.getResources(), bitmap);
    }

    public static Drawable byte2Drawable(Context mContext, byte[] b) {
        if (b != null) {
            if (b.length != 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                return new BitmapDrawable(mContext.getResources(), bitmap);
            } else {
                return null;
            }
        }
        return null;
    }

}
