package com.musk.lib.res;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 资源反射工具类
 */
public class ResReflect {
    /**
     * 根据布局的名字获取布局的id
     *
     * @param mContext
     * @param name
     * @return
     */
    public static int getLayoutId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "layout", mContext.getPackageName());
    }

    /**
     * 根据图片的名字获取图片的id
     *
     * @param mContext
     * @param name
     * @return
     */
    public static int getDrawableId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
    }

    /**
     * 根据资源名字直接拿到图片的drawable对象
     *
     * @param mContext
     * @param name
     * @return
     */
    public static Drawable getDrawable(Context mContext, String name) {
        return mContext.getResources().getDrawable(mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName()));
    }

    /**
     * 根据id名字获取id
     *
     * @param mContext
     * @param name
     * @return
     */
    public static int getId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "id", mContext.getPackageName());
    }

    /**
     * 根据string的名字获取string的id
     *
     * @param mContext
     * @param name
     * @return
     */
    public static int getStringId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "string", mContext.getPackageName());
    }

    /**
     * 根据string的名字获取string
     *
     * @param mContext
     * @param name
     * @return
     */
    public static String getString(Context mContext, String name) {
        return mContext.getString(mContext.getResources().getIdentifier(name, "string", mContext.getPackageName()));
    }

    /**
     * 根据string的名字获取string
     *
     * @param mContext
     * @param name
     * @param args
     * @return
     */
    public static String getString(Context mContext, String name, Object... args) {
        return mContext.getString(mContext.getResources().getIdentifier(name, "string", mContext.getPackageName()), args);
    }


    public static int getRawId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "raw", mContext.getPackageName());
    }

    public static int getStyleId(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "style", mContext.getPackageName());
    }

}
