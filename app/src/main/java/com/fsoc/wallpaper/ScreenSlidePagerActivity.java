package com.fsoc.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.fsoc.wallpaper.util.SettingsPreference;
import com.fsoc.wallpaper.view.ScreenSlidePageFragment;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 20;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    
    
    private String idSub = "1";
    private SettingsPreference preference;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
                .memoryCacheSize(5 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        
        
        setContentView(R.layout.activity_screen_slide);
        
        preference = SettingsPreference.getInstance(this);
        idSub = preference.getIdPack();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        //mPager.setCurrentItem(NUM_PAGES/2);
        
        
        // check status with server
        /*new Thread(new Runnable() {
			
			@Override
			public void run() {
				final String resutl = ConnectServer.checkStatus(ScreenSlidePagerActivity.this);
				runOnUiThread(new Runnable() {
					public void run() {
						//Toast.makeText(ScreenSlidePagerActivity.this, resutl + "", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).start();*/
    }

    /*@Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }*/


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return new ScreenSlidePageFragment();
            Log.d("ScreenSlidePagerAdapter","position: " + position);
            mCurrentPos = position;
        	return ScreenSlidePageFragment.newInstance(position, idSub);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private int mCurrentPos = 0;
    
    public void getMore(View v) {
    	Intent intent = new Intent(ScreenSlidePagerActivity.this, ListPackActivity.class);
		startActivityForResult(intent, 0);
    }
    public void setWallpaper(View v) {
        //SettingSystem.
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	
    	if (requestCode == 0 && resultCode == RESULT_OK) {
    		startActivity(new Intent(this, ScreenSlidePagerActivity.class));
    		finish();
    	}
    }
}