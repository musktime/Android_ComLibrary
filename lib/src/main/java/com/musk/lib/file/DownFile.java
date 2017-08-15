package com.musk.lib.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.util.Log;

public class DownFile extends Thread {

	public Context m_ctx = null;
	public Callback m_callback = null;
	public static int BUFFER_SIZE = 4096;
	private DownInfo m_info;

	public DownFile(Context context, DownInfo info,
                    Callback callback) {
		m_ctx = context;
		m_info = info;
		m_callback = callback;
	}

	@Override
	public void run() {
		if (m_info == null) {
			if (m_callback != null)
				m_callback.OnFailed(m_info, 500);
		}
		String download_filename = m_info.folder + "/" + m_info.filename;
		File file = new File(download_filename);
		if (file.exists()) {
			file.delete();
		}
		try {
			URL url = new URL(m_info.url);
			URLConnection con = url.openConnection();
			int contentLength = con.getContentLength();
			m_info.AllSize = contentLength;
			InputStream is = con.getInputStream();
			byte[] bs = new byte[BUFFER_SIZE];
			int len;
			OutputStream os = new FileOutputStream(download_filename);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				m_info.DownloadSize += len;
				m_info.percent = (double) (((double) (m_info.DownloadSize))
						% m_info.AllSize * 100);
				if (m_callback != null)
					m_callback.OnDownloading(m_info);
			}
			os.close();
			is.close();
			m_info.percent = 100;
			m_info.isFinish = true;
			if (m_callback != null)
				m_callback.OnSuccess(m_info);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean QuickDownload(String request_url, String filepath) {
		Log.i("musk", "==QuickDownload==");
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		try {
			URL url = new URL(request_url);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			byte[] bs = new byte[BUFFER_SIZE];
			int len;
			OutputStream os = new FileOutputStream(filepath);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
