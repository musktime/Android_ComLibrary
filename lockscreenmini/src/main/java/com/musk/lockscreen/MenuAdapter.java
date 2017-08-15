package com.musk.lockscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.greatwall.master.util.ResourceUtils;

/**
 * Created by admin on 2016/5/26.
 */
public class MenuAdapter extends BaseAdapter {
	Context context;
	String[] title;

	public MenuAdapter(Context context, String[] title) {
		this.context = context;
		this.title = title;
	}

	public int getCount() {
		return title.length;
	}

	public Object getItem(int position) {
		return title[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		int layoutId = ResourceUtils.getLayoutId(context, "item_pop");
		convertView = LayoutInflater.from(context).inflate(layoutId, null);
		int titleId = ResourceUtils.getId(context, "text");
		TextView menutitle = (TextView) convertView.findViewById(titleId);
		menutitle.setText(title[position]);
		return convertView;
	}
}
