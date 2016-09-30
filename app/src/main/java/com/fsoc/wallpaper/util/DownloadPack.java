package com.fsoc.wallpaper.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.fsoc.wallpaper.db.FeedReaderSQLite;
import com.fsoc.wallpaper.model.ImgPackObj;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by linhcaro on 9/16/2016.
 */
public class DownloadPack extends Activity {

    private static final String TAG = DownloadPack.class.getSimpleName();

    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;


    /**
     * Background Async Task to download file
     * */
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            super.onPreExecute();
            //showDialog(progress_bar_type);
            initPrgDlg();
            showPrgDlg();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... code) {
            Log.d(TAG, "doInBackground");
            int count;
            try {
                URL url = new URL(code[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String appPath = SettingSystem.getAppPath(DownloadPack.this);

                String fileName = appPath + code[1];
                Log.d(TAG, "doInBackground: fileName: " + fileName);
                File file = new File(fileName);
                if (!file.createNewFile()) {
                    Log.d(TAG, "doInBackground: cannot createNewFile!");
                }
                if (!file.exists()) {
                    Log.e(TAG, "doInBackground: have not: " + fileName);
                    return "ERROR";
                }

                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                if (unpackZip(fileName)) {
                    Log.d(TAG, "doInBackground: unpackZip is success!");
                    updateThumb(code[1], fileName);
                    // update thumbnail as first photo.

                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return "ERROR";
            }

            return code[1];
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            //Log.d(TAG, "onProgressUpdate: "+progress);
            // setting progress  percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String id) {
            Log.d(TAG, "onPostExecute");
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            dismissPrgDlg();

            downloadFinish(id);
        }

    }

    public void downloadFinish(String id) {

    }

    /**
     * Showing Dialog
     * */

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                //pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    private void initPrgDlg() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        //pDialog.show();
    }

    private void showPrgDlg() {
        pDialog.show();
    }

    private void dismissPrgDlg() {
        pDialog.dismiss();
    }

    private boolean unpackZip(String fullPath)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(fullPath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            // make sub folder where contain result file
            String folderExtra = fullPath + SettingSystem.EXTRA_FOLDER;
            Log.d(TAG, "unpackZip: folderExtra: " + folderExtra);
            File dir = new File(folderExtra);
            if(!dir.mkdirs()){
                Log.e(TAG, "unpackZip: Directory not created: " + folderExtra);
            }
            if (!dir.exists()) {
                Log.e(TAG, "unpackZip: have not: " + folderExtra);
                return false;
            }

            int i = 1;
            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(fullPath + filename);
                    fmd.mkdirs();
                    continue;
                }

                //FileOutputStream fout = new FileOutputStream(dir + "/" + filename);
                FileOutputStream fout = new FileOutputStream(dir + "/" + i + ".jpg");
                i++;

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private int updateThumb(String id, String fullPath) {
        String firstFile = fullPath + SettingSystem.EXTRA_FOLDER + "1.jpg";

        FeedReaderSQLite sqLite = new FeedReaderSQLite(this);
        ImgPackObj obj = sqLite.getObject(id);
        obj.setThumb(firstFile);
        return sqLite.updateObject(obj);
    }
}
