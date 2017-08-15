package com.musk.lib.constant;

import android.os.Environment;

public class Constants {
    public static final String UUID_KEY = "uuid";
    public static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ".orgmc/";
    public static final String DOWNLOAD_PATH = FILEPATH + "download/";
    public static final String IMAGECACHE_PATH = FILEPATH + "image/";
    public static final String nomedia = FILEPATH + "image/.nomedia";
    public static final String HOST = "http://api.3gu.com/index.php/";
    public static final String URL_SAVE = HOST + "mcBox/save";
    public static final String URL_INTRODUTION = HOST + "mcBox/tutorial";
    public static final String URL_VIDEO = HOST + "mcBox/video";
}
