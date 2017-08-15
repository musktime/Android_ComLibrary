package com.musk.fuzhu.log;

import android.util.Log;

public class MLog {

	private static final boolean DEBUG = true;

	public static void i(String info) {
		if (DEBUG) {
			Log.i("musk", "--" + info + "--");
		}
	}
}