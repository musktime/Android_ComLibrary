package com.musk.lockscreen;

import com.greatwall.master.util.ConfigUtil;
import com.greatwall.master.util.MLog;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class CoreService extends Service {
	private KeyguardManager mKeyguardManager = null;
	public static boolean switcher = true;

	@SuppressWarnings("deprecation")
	private KeyguardManager.KeyguardLock mKeyguardLock = null;
	private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action) && switcher) {
				MLog.i("ACTION_SCREEN_OFF");
				Intent in = new Intent(context, ScreenActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
			}
			if (Intent.ACTION_SCREEN_ON.equals(action) && switcher) {
				MLog.i("ACTION_SCREEN_ON");
				//
				context.startService(new Intent(context, CoreService.class));
				Intent in = new Intent(context, ScreenActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
		mKeyguardLock.disableKeyguard();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(0x7fffffff);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, filter);

		switcher = ConfigUtil.getBoolean(this, "wall", true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MLog.i("inKeyguardRestrictedInputMode>>"
				+ mKeyguardManager.inKeyguardRestrictedInputMode());
		return Service.START_STICKY;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		super.onDestroy();
		MLog.i("CoreService-onDestroy");
		mKeyguardLock.reenableKeyguard();
		unregisterReceiver(screenReceiver);
		startService(new Intent(this, CoreService.class));
	}
}
