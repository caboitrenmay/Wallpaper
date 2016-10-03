package com.fsoc.wallpaper.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fsoc.wallpaper.R;
import com.fsoc.wallpaper.ScreenSlidePagerActivity;
import com.fsoc.wallpaper.util.SettingSystem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Random;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class ScreenSlidePageFragment extends Fragment {

	// Store instance variables
	private String idPack;
	private int position;
	private ImageLoader imageLoader;

    private static final String ARG_POSITION = "position";
    private static final String ARG_ID_PACK = "idPack";

	// newInstance constructor for creating fragment with arguments
	public static ScreenSlidePageFragment newInstance(int position, String idPack) {
		ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_POSITION, position);
		args.putString(ARG_ID_PACK, idPack);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION, 0);
		idPack = getArguments().getString(ARG_ID_PACK);

        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance(); // Get singleton instance
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);

		// bg
		ImageView imageView = (ImageView) rootView.findViewById(R.id.bg);

		if (idPack.equals("1")) {
			final Random rand = new Random();
			//diceRoll = rand.nextInt(20);
			imageLoader.displayImage("drawable://" + ScreenSlidePagerActivity.res[position], imageView);
		} else {
			String uri = SettingSystem.getAppUri(getContext()) + idPack + SettingSystem.EXTRA_FOLDER + (position+1) + SettingSystem.FILE_TYPE;
			Log.d(TAG, "onCreateView: uri: " + uri);
			imageLoader.displayImage(uri, imageView);
		}

		return rootView;
	}

	/*public static int getFilesCount(File file) {
		File[] files = file.listFiles();
		int count = 0;
		for (File f : files)
			if (f.isDirectory())
				count += getFilesCount(f);
			else
				count++;

		return count;
	}*/
}
