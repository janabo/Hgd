package com.dk.mp.ydyx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.ydyx.R;
import com.dk.mp.ydyx.entity.WelMore;

/**
 * 子项适配.
 * @since 
 * @version2015-3-25
 * @author rm.zhao
 */
public class MoreAdapter extends BaseAdapter{
	private List<WelMore> list;
	private Context context;
	private LayoutInflater lif;

	public MoreAdapter(Context context, List<WelMore> list) {
		this.context = context;
		this.list = list;
		lif = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyView mv;
		if (convertView == null) {
			mv = new MyView();
			convertView = lif.inflate(R.layout.app_wel_items, null);
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		final WelMore process = list.get(position);
		mv.tv = (TextView) convertView.findViewById(R.id.tview);
		
		mv.img = (ImageView) convertView.findViewById(R.id.iview);
		mv.tv.setText((position + 1) + "." + process.getName());
		if ("0".equals(process.getStatus())) {
			mv.img.setImageResource(R.drawable.wel_more_do);
		} else if ("1".equals(process.getStatus())) {
			mv.img.setImageResource(R.drawable.wel_more_not);
		} else{
			mv.img.setImageResource(R.drawable.wel_more_cando);
		} 
		return convertView;
	}

	private static class MyView {
		private TextView tv;
		private ImageView img;
	}
	
}
