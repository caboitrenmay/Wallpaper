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

import com.fsoc.wallpaper.util.SettingSystem;
import com.fsoc.wallpaper.util.SettingsPreference;
import com.fsoc.wallpaper.view.ScreenSlidePageFragment;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

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

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        
        preference = SettingsPreference.getInstance(this);
        idSub = preference.getIdPack();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
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
        	return ScreenSlidePageFragment.newInstance(position, idSub);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void getMore(View v) {
    	Intent intent = new Intent(ScreenSlidePagerActivity.this, ListPackActivity.class);
		startActivityForResult(intent, 0);
    }

    /**
     * Click set wall paper.
     * @param v View
     */
    public void setWallpaper(View v) {
        setPaper(mPager.getCurrentItem());
    }

    private void setPaper(int position) {
        if (idSub.equals("1")) {
            SettingSystem.setWallpaper(this, ScreenSlidePagerActivity.res[position]);
        }
        else {
            String uri = SettingSystem.getAppPath(this) + idSub + SettingSystem.EXTRA_FOLDER + (position+1) + SettingSystem.FILE_TYPE;
            Log.d(TAG, "onCreateView: uri: " + uri);
            SettingSystem.setWallpaper(this, uri);
        }
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    	
    	if (requestCode == 0 && resultCode == RESULT_OK) {
    		startActivity(new Intent(this, ScreenSlidePagerActivity.class));
    		finish();
    	}
    }

    public static int[] res = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
            R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7,
            R.drawable.bg8, R.drawable.bg9, R.drawable.bg10, R.drawable.bg11,
            R.drawable.bg12, R.drawable.bg13, R.drawable.bg14, R.drawable.bg15,
            R.drawable.bg16, R.drawable.bg17, R.drawable.bg18, R.drawable.bg19, R.drawable.bg20 };

}