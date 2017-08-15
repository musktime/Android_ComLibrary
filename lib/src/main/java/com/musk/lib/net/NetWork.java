package com.musk.lib.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by musk on 2017/5/23.
 */

public class NetWork {

    /**
     * check network's connection
     *
     * @param context
     * @return
     */
    public static boolean isConnented(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info == null ? false : info.isAvailable();
    }

    /**
     * 判断是否是wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取本机ip
     *
     * @param context
     * @return 如果能得到ip则可以正常上网，如得到的是null则网络异常
     */
    public static String getNetworkIp(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {

                        InetAddress inetAddress = enumIpAddr.nextElement();

                        if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().toString() != null) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                return null;
            }
        }
        return null;
    }

    public static String HttpGet(String sendUrl) {
        String body = null;
        HttpURLConnection conn = null;
        BufferedReader bufferReader = null;
        try {
            URL url = new URL(sendUrl);
            conn = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            // 使用 URL 连接进行输出
            conn.setDoOutput(true);
            // 使用 URL 连接进行输入
            conn.setDoInput(true);
            // 忽略缓存
            conn.setUseCaches(false);
            // 设置连接超时时长，单位毫秒
            conn.setConnectTimeout(5 * 1000);
            // 设置读取时长，单位毫秒
            conn.setReadTimeout(5 * 1000);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置请求方式，POST or GET，注意：如果请求地址为一个servlet地址的话必须设置成POST方式
            conn.setRequestMethod("GET");
            bufferReader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String lines = "";
            StringBuffer bs = new StringBuffer();
            while ((lines = bufferReader.readLine()) != null) {
                bs.append(lines);
            }
            body = bs.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return body;
    }

    /**
     * get stream from server
     *
     * @param serverURL
     */
    public static InputStream getStream(String serverURL) throws IOException {
        URL url = new URL(serverURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn.getInputStream();
    }

    /**
     * get conn from server
     *
     * @param serverURL
     */
    public static HttpURLConnection getConnection(String serverURL) throws IOException {
        URL url = new URL(serverURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn;
    }
}
