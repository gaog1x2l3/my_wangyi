package com.example.wynews;

import com.example.wynewsfragment.MyFragment;
import com.example.wynewsfragment.NewsFragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
	private RadioGroup radioGroup;
	private MyFragment myFragment;
	private NewsFragment newsFragment;
	private RadioButton rd_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		rd_btn = (RadioButton) findViewById(R.id.ad_news);
		rd_btn.setChecked(true);
		radioGroup = (RadioGroup) findViewById(R.id.rg);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.ad_news:// 新闻
					newsFragment = new NewsFragment();
					getFragmentManager().beginTransaction().replace(R.id.fragment_replse, newsFragment).commit();
					break;
				case R.id.ad_read:// 阅读
					
					break;
				case R.id.ad_vido:// 视听

					break;
				case R.id.ad_discover:// 发现

					break;
				case R.id.ad_my:// 我
					myFragment = new MyFragment();
					getFragmentManager().beginTransaction().replace(R.id.fragment_replse, myFragment).commit();
					break;

				default:
					break;
				}
			}
		});
	}
}
