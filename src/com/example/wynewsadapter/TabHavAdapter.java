package com.example.wynewsadapter;

import java.util.List;

import com.example.wynews.R;
import com.example.wynewsobject.NewsTab_Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TabHavAdapter extends BaseAdapter {
	private List<NewsTab_Object> data;

	public void setData(List<NewsTab_Object> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public NewsTab_Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater flater = LayoutInflater.from(parent.getContext());
			convertView = flater.inflate(R.layout.item_tab_hav, parent, false);
			holder = new ViewHolder();
			holder.btn = (TextView) convertView.findViewById(R.id.tv_new_hav);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 给控件设置值
		NewsTab_Object hot = data.get(position);
		String text = hot.getTname();
		holder.btn.setText(text);
		return convertView;
	}

	private class ViewHolder {
		private TextView btn;
	}
}
