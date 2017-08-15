package com.musk.business.filedown;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import com.musk.lib.constant.Constants;
import com.musk.lib.log.CLog;
import com.musk.lib.md5.HASH;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;

public class AppDownloadUtils {
    private int NF_ID = 2015;
    private Notification nf;
    private NotificationManager nm;
    private boolean showNotify = false;
    private Bitmap appIcon;
    private Builder builder;
    public Context mContext;
    private String downloadurl;
    private String appName;
    @SuppressWarnings("unused")
    private String packageName;
    private DownloadListener downloadListener;
    private int continueCount;
    private final static String TMP = ".tmp";
    private final static String CFG = ".cfg";
    private long totalSize = 0;
    private long finishedSize = 0;
    private String savePath;
    private String fileName;
    private boolean init;

    public AppDownloadUtils(Context context) {
        this.mContext = context;
    }

    public void cearteNotify() {
        String time = System.currentTimeMillis() + "";
        NF_ID = Integer.parseInt(time.substring(time.length() / 2,
                time.length()));
        nm = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            showNotify = true;
            builder = new Builder(mContext);
            builder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            if (appIcon == null) {
                appIcon = ((BitmapDrawable) mContext.getResources()
                        .getDrawable(android.R.drawable.stat_sys_download_done))
                        .getBitmap();
            }
            builder.setLargeIcon(appIcon);
            builder.setContentTitle("正在下载: " + appName);
            builder.setContentText("已下载 0%");
            builder.setProgress(100, 0, false);
            builder.setOngoing(true);

            nf = builder.build();
            nf.flags = Notification.FLAG_NO_CLEAR;
            nf.flags = Notification.FLAG_ONGOING_EVENT;
            nf.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            nf.icon = android.R.drawable.stat_sys_download;
            nf.contentIntent = PendingIntent.getActivity(mContext, 0,
                    new Intent(), 0);

        }
    }

    public void setAppIcon(Bitmap appIcon) {
        this.appIcon = appIcon;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    if (downloadListener != null) {
                        downloadListener.onError(downloadurl);
                    }
                    if (showNotify) {
                        if (nm != null) {
                            nm.cancel(NF_ID);
                        }
                    }
                    break;

                case 4:
                    if (showNotify) {
                        if (builder != null && nm != null) {
                            builder.setContentText("已下载 " + msg.obj + "%");
                            builder.setProgress(100, (Integer) msg.obj, false);
                            nf = builder.build();
                            nm.notify(NF_ID, nf);
                        }

                    }
                    CLog.i("debug", "download process : " + msg.obj + "%");
                    break;

                case 5:
                    if (downloadListener != null) {
                        downloadListener.onDownloaded(downloadurl);
                    }
                    if (showNotify) {
                        if (nm != null) {
                            nm.cancel(NF_ID);
                        }
                    }
                    File f = new File(savePath, fileName + CFG);
                    if (f.exists()) {
                        f.delete();
                    }
                    if (DownloadService.downloadUrls.contains(downloadurl)) {
                        DownloadService.downloadUrls.remove(downloadurl);
                    }
                    try {
                        Intent it = new Intent(Intent.ACTION_VIEW);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.setDataAndType(Uri.fromFile(new File((String) msg.obj)),
                                "application/vnd.android.package-archive");
                        mContext.startActivity(it);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    };

    public void setDownloadListener(final DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte) (n >>> 0));
        os.write((byte) (n >>> 8));
        os.write((byte) (n >>> 16));
        os.write((byte) (n >>> 24));
        os.write((byte) (n >>> 32));
        os.write((byte) (n >>> 40));
        os.write((byte) (n >>> 48));
        os.write((byte) (n >>> 56));
    }

    static long readLong(InputStream is) throws IOException {
        long n = 0;
        n |= ((read(is) & 0xFFL) << 0);
        n |= ((read(is) & 0xFFL) << 8);
        n |= ((read(is) & 0xFFL) << 16);
        n |= ((read(is) & 0xFFL) << 24);
        n |= ((read(is) & 0xFFL) << 32);
        n |= ((read(is) & 0xFFL) << 40);
        n |= ((read(is) & 0xFFL) << 48);
        n |= ((read(is) & 0xFFL) << 56);
        return n;
    }

    private static int read(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new EOFException();
        }
        return b;
    }

    private void readConfig() {

        File file = new File(savePath, fileName + CFG);
        if (file.exists() && file.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                totalSize = readLong(fis);
                finishedSize = readLong(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void writeConfig() {

        File file = new File(savePath, fileName + CFG);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            writeLong(fos, totalSize);
            writeLong(fos, finishedSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startDownloadApp() {
        if (!init) {
            savePath = Constants.DOWNLOAD_PATH;
            if (!new File(savePath).exists()) {
                new File(savePath).mkdirs();
            }
            fileName = HASH.md5sum(downloadurl);
            readConfig();
            init = true;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = getHttpURLConnection();
                if (connection != null) {
                    int code = 0;
                    try {
                        code = connection.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (code < HttpURLConnection.HTTP_OK
                            || code >= HttpURLConnection.HTTP_BAD_REQUEST) {
                        connection.disconnect();
                        reDownload();
                        return;
                    }

                    int length = connection.getContentLength();
                    long progress = 0;
                    File f = new File(savePath, fileName);
                    File tmpFile = new File(savePath, fileName + TMP);
                    if (f.exists()) {
                        if (f.length() == length) {
                            connection.disconnect();

                            Message msg = new Message();
                            msg.what = 5;
                            msg.obj = f.getAbsolutePath();
                            mHandler.sendMessage(msg);
                            return;
                        } else {
                            f.delete();

                            if (tmpFile.exists()) {
                                tmpFile.delete();
                            }

                            connection.disconnect();

                            totalSize = finishedSize = 0;
                            writeConfig();
                            reDownload();
                            return;
                        }
                    }

                    if (finishedSize == 0 || totalSize == 0) {
                        totalSize = length;
                    } else {
                        if ((length + finishedSize) != totalSize) {
                            totalSize = finishedSize = 0;
                            writeConfig();

                            if (tmpFile.exists()) {
                                tmpFile.delete();
                            }

                            connection.disconnect();
                            reDownload();
                            return;
                        }
                    }

                    InputStream inputStream = null;
                    try {
                        inputStream = connection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (inputStream == null) {
                        connection.disconnect();
                        reDownload();
                        return;
                    }

                    RandomAccessFile raf = null;
                    try {
                        raf = new RandomAccessFile(tmpFile, "rw");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (raf == null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.disconnect();
                        mHandler.sendEmptyMessage(3);
                        return;
                    }

                    long tmpLen = 0;
                    try {
                        tmpLen = raf.length();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (finishedSize > tmpLen) {
                        totalSize = finishedSize = 0;
                        writeConfig();

                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.disconnect();

                        try {
                            raf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (tmpFile.exists()) {
                            tmpFile.delete();
                        }
                        reDownload();
                        return;
                    }

                    if (finishedSize == 0) {
                        Message msg2 = new Message();
                        msg2.obj = 0;
                        msg2.what = 4;
                        mHandler.sendMessage(msg2);
                    }

                    byte[] buffer = new byte[8192];
                    int len = 0;
                    try {
                        raf.seek(finishedSize);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        while ((len = inputStream.read(buffer)) != -1) {
                            raf.write(buffer, 0, len);
                            finishedSize += len;
                            writeConfig();
                            int newProgress = (int) ((100 * finishedSize) / totalSize);
                            if (newProgress - 1 > progress) {
                                progress = newProgress;
                                Message msg = new Message();
                                msg.obj = newProgress;
                                msg.what = 4;
                                mHandler.sendMessage(msg);
                            }
                        }
                        inputStream.close();
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (finishedSize == totalSize || totalSize < 0) {
                        if (tmpFile.renameTo(new File(savePath, fileName))) {
                            Message msg = new Message();
                            msg.what = 5;
                            msg.obj = new File(savePath, fileName)
                                    .getAbsolutePath();
                            mHandler.sendMessage(msg);
                        } else {
                            mHandler.sendEmptyMessage(3);
                        }
                    } else {
                        mHandler.sendEmptyMessage(3);
                    }
                    connection.disconnect();
                } else {
                    reDownload();
                }
            }
        }).start();

    }

    private void reDownload() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (continueCount > 0) {
            continueCount--;
            startDownloadApp();
        } else {
            mHandler.sendEmptyMessage(3);
        }
    }

    private HttpURLConnection getHttpURLConnection() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(downloadurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(true);
            if (this.finishedSize > 0) {
                connection.setRequestProperty("RANGE", "bytes="
                        + this.finishedSize + "-");
            }
            connection.setRequestProperty("User-Agent", "AnyDownloader");
            connection.setReadTimeout(30 * 1000);
            connection.setUseCaches(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public interface DownloadListener {
        void onError(String url);

        void onInstalled();

        void onDownloaded(String url);
    }
}
