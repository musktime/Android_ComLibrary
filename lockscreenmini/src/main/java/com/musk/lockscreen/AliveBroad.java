package com.musk.lockscreen;

import com.musk.lib.device.DeviceState;
import com.musk.lib.log.CLog;
import com.musk.lockscreen.util.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AliveBroad extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = DeviceState.isServiceWork(context,
                Constants.EXCEPT_SERVICE);
        if (isRunning) {
            try {
                context.stopService(new Intent(context, CoreService.class));
            } catch (Exception e) {
                CLog.i(e.getMessage());
            }
        } else {
            context.startService(new Intent(context, CoreService.class));
        }
    }
}