package com.musk.lib.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.musk.lib.R;

public class MyToast {
    public static void show(Context context, String info) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_toast, null);
        TextView title = (TextView) view.findViewById(R.id.tv_tos);
        title.setText(info);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
