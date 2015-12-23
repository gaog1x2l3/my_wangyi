package com.example.wynewsfragment;

import java.util.List;

import org.json.JSONObject;

import com.example.wynews.R;
import com.example.wynewsadapter.TabHavAdapter;
import com.example.wynewsobject.NewsTab_Object;
import com.example.wynewstool.Constants;
import com.example.wynewstool.DownUtil;
import com.example.wynewstool.DownUtil.OnDownCompelet;
import com.example.wynewstool.GetData;
import com.example.wynewsview.MyGridView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends Fragment {
	private ViewPager viewpager;
	private MyGridView gv_news;
	private List<NewsTab_Object> tab_havList;
	private TabHavAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		viewpager = (ViewPager) view.findViewById(R.id.vp_news);
		gv_news = (MyGridView) view.findViewById(R.id.gv_tab_hav);
		adapter = new TabHavAdapter();
		DownUtil.down(DownUtil.TYPE_JSON, Constants.TAB_HAV, new OnDownCompelet() {

			@Override
			public void downCompelet(String url, Object obj) {
				System.out.println(obj);
				tab_havList = GetData.getNewsList((JSONObject) obj);
				adapter.setData(tab_havList);
				gv_news.setAdapter(adapter);
			}
		});
		return view;
	}
}
