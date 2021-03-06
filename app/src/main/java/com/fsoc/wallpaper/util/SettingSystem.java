package com.fsoc.wallpaper.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by linhcaro on 9/16/2016.
 */
public class SettingSystem {
    private static final String TAG = SettingSystem.class.getSimpleName();

    private static final String APP_FOLDER = "/wallpaper";
    private static final String URI_PREFIX = "file://";

    public static final String EXTRA_FOLDER = "f/";
    public static final String FILE_TYPE = ".jpg";

    private Context mContext;

    public SettingSystem(Context mContext) {
        this.mContext = mContext;
    }

    public boolean initSetting() {
        /* Checks if external storage is available for read and write */
        if (ExternalStorage.isExternalStorageWritable()) { // OK
            /* Get the directory for the app's private pictures directory. */
            File dir = ExternalStorage.getAlbumStorageDir(mContext, SettingSystem.APP_FOLDER);
            // Check file exist
            if(!dir.exists()){
                Log.e(TAG, "initSetting: have not directory");
                return false;
            }
            Log.d(TAG, "initSetting: " + dir.getPath());
        }
        else { // NOT OK, EXIT
            Log.e(TAG, "initSetting: have not ExternalStorage");
            return false;
        }
        return true;
    }

    @NonNull
    public static String getAppPath(Context context) {
        File dir = ExternalStorage.getAlbumStorageDir(context, SettingSystem.APP_FOLDER);
        return dir.getPath() + File.separator;
    }

    public static String getAppUri(Context context) {
        return URI_PREFIX + getAppPath(context);
    }

    public static void setWallpaper(final Activity activity, final int res) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(activity);
                    myWallpaperManager.setResource(res);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Set wallpaper done!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void setWallpaper(final Activity activity, final String photoPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WallpaperManager myWallpaperManager   = WallpaperManager.getInstance(activity);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                    myWallpaperManager.setBitmap(bitmap);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Set wallpaper done!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
