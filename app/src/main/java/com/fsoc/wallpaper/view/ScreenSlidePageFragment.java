package com.fsoc.wallpaper.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fsoc.wallpaper.R;
import com.fsoc.wallpaper.util.SettingSystem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Random;

public class ScreenSlidePageFragment extends Fragment {

	// Store instance variables
	private String idPack;
	private int position;
	private ImageLoader imageLoader;
	// random position
	//private int diceRoll;

	// newInstance constructor for creating fragment with arguments
	public static ScreenSlidePageFragment newInstance(int page, String title) {
		ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt("someInt", 0);
		idPack = getArguments().getString("someTitle");

		imageLoader = ImageLoader.getInstance(); // Get singleton instance
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);

		TextView tvLabel = (TextView) rootView.findViewById(R.id.text);
		tvLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if (idPack.equals("1")) {
                    SettingSystem.setWallpaper(getActivity().getApplicationContext(), res[position]);
                }
                else {
                    String uri = SettingSystem.PATH + idPack + SettingSystem.EXTRA_FOLDER + (position+1) + SettingSystem.FILE_TYPE;
                    SettingSystem.setWallpaper(getActivity().getApplicationContext(), uri);
                }

			}
		});

		// bg
		ImageView imageView = (ImageView) rootView.findViewById(R.id.bg);
		//imageLoader.displayImage(null, imageView);

		if (idPack.equals("1")) {
			final Random rand = new Random();
			//diceRoll = rand.nextInt(20);
			imageLoader.displayImage("drawable://" + res[position], imageView);
		} else {
			String uriDir = SettingSystem.PATH + idPack + SettingSystem.EXTRA_FOLDER;
			//int size = new File(uriDir).listFiles().length;
			//Random rand = new Random();
			//diceRoll = rand.nextInt(size);
			String uri = SettingSystem.URI_PATH + idPack + SettingSystem.EXTRA_FOLDER + (position+1) + SettingSystem.FILE_TYPE;
			imageLoader.displayImage(uri, imageView);
		}

		return rootView;
	}

	private int[] res = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
			R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7,
			R.drawable.bg8, R.drawable.bg9, R.drawable.bg10, R.drawable.bg11,
			R.drawable.bg12, R.drawable.bg13, R.drawable.bg14, R.drawable.bg15,
			R.drawable.bg16, R.drawable.bg17, R.drawable.bg18, R.drawable.bg19, R.drawable.bg20 };

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
