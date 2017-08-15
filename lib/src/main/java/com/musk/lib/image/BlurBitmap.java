package com.musk.lib.image;

import android.content.Context;
import android.graphics.Bitmap;

public class BlurBitmap {
    /**
     * 图片缩放比例
     */
    private static final float BITMAP_SCALE = 0.1f;
    /**
     * 最大模糊度(在0.0到25.0之间)
     */
    @SuppressWarnings("unused")
    private static final float BLUR_RADIUS = 25f;
    static Bitmap inputBitmap, blurBitmap;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blur(Context context, Bitmap image) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        if (inputBitmap != null) {
            if (!inputBitmap.isRecycled()) {
                inputBitmap.recycle();
                System.gc();
            }
        }

        // 将缩小后的图片做为预渲染的图片。
        inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        if (blurBitmap != null) {
            if (!blurBitmap.isRecycled()) {
                blurBitmap.recycle();
                System.gc();
            }
        }
        blurBitmap = FastBlur.doBlur(inputBitmap, 25, true);
        /*
         * PhotoCompressUtils photoCompress2 = new PhotoCompressUtils(); //
		 * bitmap = photoCompress2.CompressionBigBitmap(bitmap); // 压缩获取的图像 //
		 * 创建一张渲染后的输出图片。
		 *//*
             * if (outputBitmap != null) { outputBitmap.recycle(); System.gc();
			 * }
			 *//*
                 * outputBitmap =
				 * Bitmap.createBitmap(photoCompress2.CompressionBigBitmap
				 * (inputBitmap));
				 *
				 *
				 * // 创建RenderScript内核对象 RenderScript rs =
				 * RenderScript.create(context); // 创建一个模糊效果的RenderScript的工具对象
				 * ScriptIntrinsicBlur blurScript =
				 * ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
				 *
				 * // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。 //
				 * 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。 Allocation
				 * tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
				 * Allocation tmpOut = Allocation.createFromBitmap(rs,
				 * outputBitmap);
				 *
				 * // 设置渲染的模糊程度, 25f是最大模糊度 blurScript.setRadius(BLUR_RADIUS); //
				 * 设置blurScript对象的输入内存 blurScript.setInput(tmpIn); //
				 * 将输出数据保存到输出内存中 blurScript.forEach(tmpOut);
				 *
				 * // 将数据填充到Allocation中 tmpOut.copyTo(outputBitmap);
				 */

        return blurBitmap;
    }
}
