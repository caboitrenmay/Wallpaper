package com.fsoc.wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fsoc.wallpaper.util.SettingSystem;

import java.io.File;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initSetting();

		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(MainActivity.this, ScreenSlidePagerActivity.class));
				finish();
			}
		};
		handler.postDelayed(runnable, 1000);
	}

	private void initSetting() {
		File dir = new File(SettingSystem.PATH);
		boolean d = false;
		if(dir.exists()){
			d = dir.mkdirs();
		}
		Log.d(TAG, "doInBackground: mkdirs: " +d);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
