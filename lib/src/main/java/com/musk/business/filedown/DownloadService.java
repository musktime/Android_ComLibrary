package com.musk.business.filedown;

import java.util.ArrayList;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;
import com.musk.business.imageload.ImageDownLoader;

public class DownloadService extends Service {
	public static ArrayList<String> downloadUrls = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String appName = intent.getStringExtra("name");
			String url = intent.getStringExtra("url");
			String icon_url = intent.getStringExtra("icon_url");
			Bitmap appIcon = ImageDownLoader.getInstance(this).downloadImage(icon_url, null);
			String packageName = intent.getStringExtra("packageName");
			downloadUrls.add(url);
			AppDownloadUtils appDownload = new AppDownloadUtils(this);
			appDownload.setDownloadurl(url);
			appDownload.setAppName(appName);
			appDownload.setAppIcon(appIcon);
			if (!TextUtils.isEmpty(packageName)) {
				appDownload.setPackageName(packageName);
			}
			appDownload.cearteNotify();
			appDownload.startDownloadApp();
			Toast.makeText(this, appName + "开始下载", Toast.LENGTH_SHORT).show();
		}

		return super.onStartCommand(intent, flags, startId);
	}

}
