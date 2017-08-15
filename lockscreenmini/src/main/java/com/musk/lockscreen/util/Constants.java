package com.musk.lockscreen.util;

import android.os.Environment;

public class Constants {
	public static final String FILEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/" + ".orgmc/";
	public static final String IMAGECACHE_PATH = FILEPATH + "image/";
	public static final String nomedia = FILEPATH + "image/.nomedia";

	public static final String EXCEPT_SERVICE = "com.greatwall.master.CoreService";
}