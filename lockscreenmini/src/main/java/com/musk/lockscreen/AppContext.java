package com.musk.lockscreen;

import java.util.List;
import com.greatwall.master.util.MLog;
import com.greatwall.master.util.Utils;
import com.qq.e.ads.nativ.NativeAD.NativeAdListener;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class AppContext extends Application implements NativeAdListener {
	private NativeAD nativeAD;
	private NativeADDataRef adItem;
	private Context context;
	private String appId, posId;

	private static AppContext appContext;

	public static AppContext getInstance() {
		return appContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		appContext = this;
		refreshAD();
	}

	/**
	 * ˢ�ºͼ��ع������
	 */
	public void refreshAD() {
		new Thread(new Runnable() {
			public void run() {
				MLog.i("refreshAD");
				if (nativeAD == null) {
					if (TextUtils.isEmpty(appId)) {
						// �������ļ��ж�ȡ���id
						appId = Utils.getMetaValue(context, "APP_ID");
						String[] tmps = appId.split("_");
						appId = tmps[0];
					}
					MLog.i("appId>>" + appId);
					if (TextUtils.isEmpty(posId)) {
						// �������ļ��ж�ȡ���λid
						posId = Utils.getMetaValue(context, "POS_ID");
						String[] tmps = posId.split("_");
						posId = tmps[0];
					}
					nativeAD = new NativeAD(context, appId, posId,
							AppContext.this);
				}
				// ���ش���Ϊ2���ೢ�Լ���
				int count = 2;
				nativeAD.loadAD(count);
			}
		}).start();
	}

	public NativeADDataRef getNativeAd() {
		MLog.i("getNativeAd" + adItem);
		return adItem;
	}

	// for NativeAdListener
	public void onADLoaded(List<NativeADDataRef> arg0) {
		if (arg0 != null) {
			MLog.i("onADLoaded=" + arg0.size());
			if (arg0.size() > 0) {
				adItem = arg0.get(0);
			}
		}
	}

	public void onADStatusChanged(NativeADDataRef arg0) {
	}

	public void onNoAD(int arg0) {
		MLog.i("onNoAD");
	}
}