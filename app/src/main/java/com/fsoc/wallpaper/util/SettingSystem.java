package com.fsoc.wallpaper.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by linhcaro on 9/16/2016.
 */
public class SettingSystem {
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/wallpaper/pokemon/";
    public static final String EXTRA_FOLDER = "f/";
    public static final String URI_PATH = "file:///mnt/sdcard/wallpaper/pokemon/";

    public static final String FILE_TYPE = ".jpg";

    public static void setWallpaper(Context context, int res) {
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
        try {
            myWallpaperManager.setResource(res);
            Toast.makeText(context, "Set wallpaper done!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setWallpaper(Context context, String photoPath) {
        WallpaperManager myWallpaperManager   = WallpaperManager.getInstance(context);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        try {
            myWallpaperManager.setBitmap(bitmap);
            Toast.makeText(context, "Set wallpaper done!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
