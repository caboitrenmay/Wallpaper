package com.fsoc.wallpaper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fsoc.wallpaper.util.ConnectServer;
import com.fsoc.wallpaper.util.JsonResultHandler;
import com.fsoc.wallpaper.util.SettingSystem;

import org.json.JSONException;
import org.json.JSONObject;

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

public class EnterCodeActivity extends Activity {
	 
    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_enter_code);
		
		final EditText code = (EditText) findViewById(R.id.enter_code);
		Button send = (Button) findViewById(R.id.send);
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!code.getText().toString().trim().equals("")) {
					// starting new Async Task
					new DownloadFileFromURL().execute(code.getText().toString().trim());
				}
				else {
					Toast.makeText(EnterCodeActivity.this, "Xin hãy nhập mã code!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	 /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type: // we set this to 0
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
    }
    
    private boolean unpackZip(String path, String zipname)
    {       
         InputStream is;
         ZipInputStream zis;
         try 
         {
             String filename;
             is = new FileInputStream(path + zipname);
             zis = new ZipInputStream(new BufferedInputStream(is));          
             ZipEntry ze;
             byte[] buffer = new byte[1024];
             int count;
             
             // make sub folder where contain result file
             File dir = new File(path + zipname + SettingSystem.EXTRA_FOLDER);
      	     if(dir.exists() == false){
      	    	dir.mkdirs();
      	     }

      	     int i = 1;
             while ((ze = zis.getNextEntry()) != null) 
             {
                 // zapis do souboru
                 filename = ze.getName();

                 // Need to create directories if not exists, or
                 // it will generate an Exception...
                 if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
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
    
    
    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... code) {
        	
        	String result = ConnectServer.getPack(code[0], EnterCodeActivity.this);
        	String s_url = "";
        	String id = "";
			
			JsonResultHandler resultHandler = new JsonResultHandler(result);
			if (resultHandler.isOk()) {
				JSONObject jsonObject = resultHandler.getjObj();
				
				try {
					JSONObject resJs  = jsonObject.getJSONObject("result");
					s_url = resJs.getString("pack");
					id = resJs.getString("id");
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
			}
			else {
				return "";
			}
        	
            int count;
            try {
                URL url = new URL(s_url);
                //String id = f_url[1];
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
 
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
 
                //File root = android.os.Environment.getExternalStorageDirectory();
    	        //File dir = new File(root.getAbsolutePath() + "/lichaodai");
                File dir = new File(SettingSystem.PATH);
    	        if(dir.exists() == false){
    	             dir.mkdirs();  
    	        }
    	        
                // Output stream
                OutputStream output = new FileOutputStream(SettingSystem.PATH + id);
 
                byte data[] = new byte[1024];
 
                long total = 0;
 
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
 
                    // writing data to file
                    output.write(data, 0, count);
                }
 
                // flushing output
                output.flush();
 
                // closing streams
                output.close();
                input.close();
 
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
 
            return id;
        }
 
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }
 
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String id) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            
            if (!id.equals("")) {
            	unpackZip(SettingSystem.PATH, id);
            	
            	Intent output = new Intent();
                output.putExtra("id_package", id);
                
                setResult(RESULT_OK, output);
                finish();
            }
            else {
				Toast.makeText(EnterCodeActivity.this, "Mã code ko đúng!", Toast.LENGTH_SHORT).show();
			}
        }
 
    }
}
