package com.musk.lockscreen;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.musk.business.imageload.ImageDownLoader;
import com.musk.lib.image.BlurBitmap;
import com.musk.lib.log.CLog;
import com.musk.lib.res.ResReflect;
import com.musk.lib.storage.ShareSave;
import com.musk.lib.ui.CircleProgressBar;
import com.musk.lib.ui.MyDialog;
import com.musk.lib.ui.SlideRLayout;
import com.musk.lockscreen.util.Screen;
import com.qq.e.ads.nativ.NativeADDataRef;

public class ScreenActivity extends Activity {

    // wall
    private SlideRLayout rightView;
    private RelativeLayout rootView;
    private TextView tvTime, tvDay, tvPower;
    private ImageView ivSlid;
    // adInfo
    private RelativeLayout adLayout;
    private TextView adTitle, adDes;
    private ImageView adLogo, adImg;
    private Button adDown;
    private CircleProgressBar powerProgressBar;
    private ImageView ivCharging;
    private TextView chargingTip1, chargingTip2;

    // popupWin
    private PopupWindow popupWindow;
    private ListView lv_group;
    private String[] menutitle = {"关闭"};

    private NativeADDataRef nativeAd;
    private ImageDownLoader imageLoader;
    private Context context;
    private boolean isCharging = false;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                nativeAd = AppContext.getInstance().getNativeAd();
                showAD();
            } else if (msg.what == 2) {
                if (adLogo != null) {
                    adLogo.setImageBitmap((Bitmap) msg.obj);
                }
            } else if (msg.what == 3) {
                if (adImg != null) {
                    adImg.setImageBitmap((Bitmap) msg.obj);
                    adLayout.setVisibility(View.VISIBLE);
                }
            } else if (msg.what == 4) {
                updateTime(tvPower, null);
                powerProgressBar.setVisibility(View.VISIBLE);
                powerProgressBar.setProgress(msg.arg1);
                ivCharging.setVisibility(View.VISIBLE);
                chargingTip1.setVisibility(View.VISIBLE);
                chargingTip2.setVisibility(View.VISIBLE);
                chargingTip2.setTextColor(Color.parseColor("#41B51E"));
                chargingTip2.setText("已安全充电" + msg.arg1 + "%");
                tvTime.setVisibility(View.GONE);
                tvDay.setVisibility(View.GONE);
            } else if (msg.what == 5) {
                powerProgressBar.setVisibility(View.GONE);
                ivCharging.setVisibility(View.GONE);
                chargingTip1.setVisibility(View.GONE);
                chargingTip2.setVisibility(View.GONE);
                tvTime.setVisibility(View.VISIBLE);
                tvDay.setVisibility(View.VISIBLE);
            } else {
                ;
            }
        }
    };
    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                if (isCharging) {
                    updateTime(tvPower, null);
                } else {
                    updateTime(tvTime, tvDay);
                }
            }
        }
    };
    private BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                // 获取当前电量
                int level = intent.getIntExtra("level", 0);
                // 电量的�?刻度
                int scale = intent.getIntExtra("scale", 100);
                // 把它转成百分�?
                int percent = (level * 100) / scale;
                tvPower.setText(percent + "%");
                CLog.i("ACTION_BATTERY_CHANGED");

                int status = intent.getIntExtra("status",
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    Message msg = new Message();
                    msg.what = 4;
                    msg.arg1 = percent;
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(5);
                }
            }
        }
    };

    private void registerReceivers() {
        // time
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        timeFilter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(timeReceiver, timeFilter);
        // power
        IntentFilter powerFilter = new IntentFilter();
        powerFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(powerReceiver, powerFilter);
    }

    private void unRegisterReceivers() {
        unregisterReceiver(timeReceiver);
        unregisterReceiver(powerReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigation();
        context = this;
        int layoutId = ResReflect.getLayoutId(context, "layout_screen");
        setContentView(layoutId);
        init();
        CLog.i("onCreate>>" + isCharging);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 针对顶部状态栏和底部虚拟按键的隐藏全屏
     */
    private void setNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (!ViewConfiguration.get(this).hasPermanentMenuKey()) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            getWindow().getDecorView()
                    .setSystemUiVisibility(systemUiVisibility);
        }
    }

    @SuppressWarnings("deprecation")
    private void init() {
        initPopwin();
        int slidRightId = ResReflect.getId(context, "slid_layout");
        rightView = (SlideRLayout) findViewById(slidRightId);
        rightView
                .setOnReleasedListener(new SlideRLayout.OnReleasedListener() {
                    public void onReleased() {
                        ScreenActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                    }
                });
        int rootId = ResReflect.getId(context, "layout_root");
        rootView = (RelativeLayout) findViewById(rootId);
        Bitmap tmp = Screen.getDefaultPaper(context);
        Log.i("musk", "==tmp==" + (tmp == null));
        if (tmp != null) {
            Bitmap bitmap = BlurBitmap.blur(context, tmp);

            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                rootView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            } else {
                rootView.setBackground(new BitmapDrawable(bitmap));
            }
        }
        int progressId = ResReflect.getId(context, "power_progresser");
        powerProgressBar = (CircleProgressBar) findViewById(progressId);
        powerProgressBar.setMax(100);
        int chargingId = ResReflect.getId(context, "iv_charging");
        ivCharging = (ImageView) findViewById(chargingId);
        int anim_charge_id = ResReflect.getDrawableId(context, "charging");
        ivCharging.setBackgroundResource(anim_charge_id);
        AnimationDrawable animCharge = (AnimationDrawable) ivCharging
                .getBackground();
        animCharge.start();

        int tip1 = ResReflect.getId(context, "charging_tip1");
        chargingTip1 = (TextView) findViewById(tip1);
        int tip2 = ResReflect.getId(context, "charging_tip2");
        chargingTip2 = (TextView) findViewById(tip2);
        int timeId = ResReflect.getId(context, "tv_time");
        tvTime = (TextView) findViewById(timeId);
        int dayId = ResReflect.getId(context, "tv_day");
        tvDay = (TextView) findViewById(dayId);
        int powerId = ResReflect.getId(context, "tv_power");
        tvPower = (TextView) findViewById(powerId);
        updateTime(tvTime, tvDay);

        // ad
        int adLayoutId = ResReflect.getId(context, "nativeADContainer");
        adLayout = (RelativeLayout) findViewById(adLayoutId);
        int ad_titleId = ResReflect.getId(context, "ad_title");
        adTitle = (TextView) adLayout.findViewById(ad_titleId);
        int ad_desId = ResReflect.getId(context, "ad_des");
        adDes = (TextView) adLayout.findViewById(ad_desId);
        int ad_logoId = ResReflect.getId(context, "ad_logo");
        adLogo = (ImageView) adLayout.findViewById(ad_logoId);
        int posterId = ResReflect.getId(context, "img_poster");
        adImg = (ImageView) adLayout.findViewById(posterId);
        adImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (nativeAd != null) {
                    nativeAd.onClicked(v);
                }
            }
        });
        int ad_downId = ResReflect.getId(context, "ad_action");
        adDown = (Button) adLayout.findViewById(ad_downId);
        adDown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (nativeAd != null) {
                    nativeAd.onClicked(v);
                }
            }
        });
        adLayout.setVisibility(View.INVISIBLE);

        int slidId = ResReflect.getId(context, "imv_sliding");
        ivSlid = (ImageView) findViewById(slidId);
        int anim_slid = ResReflect.getDrawableId(context, "sliding");
        ivSlid.setBackgroundResource(anim_slid);
        AnimationDrawable anim = (AnimationDrawable) ivSlid.getBackground();
        anim.start();

        // 注册时间和电源广播
        registerReceivers();
        // 获取预加载的广告数据
        nativeAd = AppContext.getInstance().getNativeAd();
        if (nativeAd == null) {
            // 隐藏广告视图
            handler.sendEmptyMessageDelayed(1, 500);
        } else {
            // 展示广告数据
            showAD();
        }
        // 刷新广告以备下载加载
        AppContext.getInstance().refreshAD();
    }

    @SuppressWarnings("deprecation")
    private void initPopwin() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout_pop = ResReflect.getLayoutId(context, "layout_pop");
        View popview = layoutInflater.inflate(layout_pop, null);

        int lvId = ResReflect.getId(context, "lvGroup");
        lv_group = (ListView) popview.findViewById(lvId);
        // 加载数据
        MenuAdapter adapter = new MenuAdapter(ScreenActivity.this, menutitle);
        lv_group.setAdapter(adapter);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int bmpW = dm.widthPixels;// 获取分辨率宽度
        // 创建一个PopuWidow对象
        popupWindow = new PopupWindow(popview, bmpW / 3,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
    }

    private void showpopWin(View v) {
        int xPos = popupWindow.getWidth() - v.getWidth() + 10;
        popupWindow.showAsDropDown(v, -xPos, -v.getHeight() + 10);
        // popupWindow.showAsDropDown(parent);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        //
                        showDialog(true);
                        break;
                }
            }
        });
    }

    private void showAD() {
        if (nativeAd != null) {
            // exposure
            int adContainner = ResReflect
                    .getId(context, "nativeADContainer");
            nativeAd.onExposured(findViewById(adContainner));

            // show
            adTitle.setText(nativeAd.getTitle());
            adDes.setText(nativeAd.getDesc());
            adDown.setText(getADButtonText(nativeAd));

            if (imageLoader == null) {
                imageLoader = ImageDownLoader.getInstance(context);
            }

            Bitmap imgLogo = imageLoader.downloadImage(nativeAd.getIconUrl(),
                    new ImageDownLoader.onImageLoaderListener() {
                        public void onImageLoader(Bitmap bitmap, String url) {
                            if (bitmap != null) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            }
                        }
                    });
            if (imgLogo != null) {
                adLogo.setImageBitmap(imgLogo);
            }
            Bitmap imgPost = imageLoader.downloadImage(nativeAd.getImgUrl(),
                    new ImageDownLoader.onImageLoaderListener() {
                        public void onImageLoader(Bitmap bitmap, String url) {
                            if (bitmap != null) {
                                Message msg = new Message();
                                msg.what = 3;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            }
                        }
                    });
            if (imgPost != null) {
                adImg.setImageBitmap(imgPost);
                adLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateTime(TextView timeView, TextView dayView) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号�?
        String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String tmp = String.valueOf(c.get(Calendar.MINUTE));
        String minute = tmp.length() == 2 ? tmp : "0" + tmp;

        if ("1".equals(week)) {
            week = "日";
        } else if ("2".equals(week)) {
            week = "一";
        } else if ("3".equals(week)) {
            week = "二";
        } else if ("4".equals(week)) {
            week = "三";
        } else if ("5".equals(week)) {
            week = "四";
        } else if ("6".equals(week)) {
            week = "五";
        } else if ("7".equals(week)) {
            week = "六";
        }
        String simpleDate = month + "月" + day + "日" + "   周" + week;
        String simpleTime = hour + ":" + minute;
        if (timeView != null) {
            timeView.setText(simpleTime);
        }
        if (dayView != null) {
            dayView.setText(simpleDate);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, CoreService.class));
        unRegisterReceivers();
    }

    public void clickMore(View v) {
        /**
         * 显示右上角浮动菜单
         *
         * @param parent
         */
        setNavigation();
        showpopWin(v);
    }

    private void showDialog(final boolean forever) {
        MyDialog.Builder alertBuilder = new MyDialog.Builder(this);
        int posiId = ResReflect.getStringId(context, "alert_confirm");
        alertBuilder.setPoistiveButton(posiId,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        disEnableScreen(forever);
                    }
                });
        int nageId = ResReflect.getStringId(context, "alert_cancel");
        alertBuilder.setNagetiveButton(nageId,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertBuilder.create(new OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {

            }
        }).show();
    }

    private void disEnableScreen(boolean forever) {
        if (forever) {
            ShareSave.setBoolean(context, "wall", false);
            CoreService.switcher = false;
        }
        ScreenActivity.this.finish();
    }

    public String getADButtonText(NativeADDataRef adItem) {
        if (adItem == null) {
            return "……";
        }
        if (!adItem.isAPP()) {
            return "详情";
        }
        switch (adItem.getAPPStatus()) {
            case 0:
                return "下载";
            case 1:
                return "启动";
            case 2:
                return "更新";
            case 4:
                return "下载中";
            case 8:
                return "安装";
            case 16:
                return "下载";
            default:
                return "详情";
        }
    }
}
