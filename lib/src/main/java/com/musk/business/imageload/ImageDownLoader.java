package com.musk.business.imageload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.musk.lib.constant.Constants;
import com.musk.lib.image.DrawableUtils;
import com.musk.lib.md5.HASH;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * @author 庄宏岩 图片下载管理类
 */
public class ImageDownLoader {

    public static ImageDownLoader imageDownLoader;

    public synchronized static ImageDownLoader getInstance(Context mContext) {
        if (imageDownLoader == null) {
            imageDownLoader = new ImageDownLoader(mContext);
        }
        return imageDownLoader;
    }

    /**
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
     */
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 下载Image的线程池
     */
    private ExecutorService mImageThreadPool = null;

    public ImageDownLoader(Context context) {

        if (Build.VERSION.SDK_INT >= 9) {
            // 获取系统分配给每个应用程序的最大内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int mCacheSize = maxMemory / 8;
            // 给LruCache分配1/8
            mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
                // 必须重写此方法，来测量Bitmap的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

            };
        }

        String path = Constants.IMAGECACHE_PATH;
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        if (!new File(Constants.nomedia).exists()) {
            try {
                new File(Constants.nomedia).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     *
     * @return
     */
    public ExecutorService getThreadPool() {
        if (mImageThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (mImageThreadPool == null) {
                    mImageThreadPool = Executors.newFixedThreadPool(10);
                }
            }
        }

        return mImageThreadPool;

    }

    /**
     * 添加Bitmap到内存缓存
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            if (mMemoryCache != null) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache != null) {
            return mMemoryCache.get(key);
        }
        return null;
    }

    /**
     * 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取，SD卡或者手机缓存 没有就去下载
     *
     * @param url
     * @param listener
     * @return
     */
    public Bitmap downloadImage(final String url, final onImageLoaderListener listener) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        final String fileName = HASH.md5sum(url);
        Bitmap bitmap = showCacheBitmap(fileName);
        if (bitmap != null) {
            return bitmap;
        } else {
            getThreadPool().execute(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFormUrl(url, fileName);
                    if (bitmap != null) {
                        if (listener != null) {
                            listener.onImageLoader(bitmap, url);
                        }
                        // 将Bitmap 加入内存缓存
                        addBitmapToMemoryCache(fileName, bitmap);
                    }
                }
            });
        }

        return null;
    }

    /**
     * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
     *
     * @param fileName
     * @return
     */
    public Bitmap showCacheBitmap(String fileName) {
        if (getBitmapFromMemCache(fileName) != null) {
            return getBitmapFromMemCache(fileName);
        } else if (new File(Constants.IMAGECACHE_PATH, fileName).exists() && new File(Constants.IMAGECACHE_PATH, fileName).length() != 0) {
            // 从SD卡获取手机里面获取Bitmap
            Bitmap bitmap = DrawableUtils.getBitmap(Constants.IMAGECACHE_PATH + fileName);

            // 将Bitmap 加入内存缓存
            addBitmapToMemoryCache(fileName, bitmap);
            return bitmap;
        }

        return null;
    }

    /**
     * 从Url中获取Bitmap
     *
     * @param url
     * @param fileName
     * @return
     */
    private Bitmap getBitmapFormUrl(String url, String fileName) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        InputStream inStream = null;
        FileOutputStream outputStream = null;
        try {
            File file = new File(Constants.IMAGECACHE_PATH, fileName);
            File tempFile = new File(Constants.IMAGECACHE_PATH, fileName + ".temp");

            if (file.exists()) {
                return DrawableUtils.getBitmap(Constants.IMAGECACHE_PATH + fileName);
            }

            long totalSize = 0;
            long downloadSize = 0;

            URL mImageUrl = new URL(url);
            con = (HttpURLConnection) mImageUrl.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(true);

            inStream = con.getInputStream();
            outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            totalSize = con.getContentLength();
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                downloadSize += len;
            }

            if (totalSize == downloadSize) {
                tempFile.renameTo(file);
                bitmap = DrawableUtils.getBitmap(Constants.IMAGECACHE_PATH + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 取消正在下载的任务
     */
    public synchronized void cancelTask() {
        if (mImageThreadPool != null) {
            mImageThreadPool.shutdownNow();
            mImageThreadPool = null;
        }
    }

    /**
     * 异步下载图片的回调接口
     *
     * @author len
     */
    public interface onImageLoaderListener {
        void onImageLoader(Bitmap bitmap, String url);
    }

}
