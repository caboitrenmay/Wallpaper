package com.fsoc.wallpaper.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ConnectServer {

	private final static String SERVER = "http://103.48.83.25:8033/";
	//private final static String STATUS = "call.xml";
	private final static String LIST = "call.xml";
	private final static String DOWNLOAD = "download.php";

	private static final String TAG = "ConnectServer";

	public static String connect_simple_ori_get(String url) {
		Log.d(TAG, "connect_simple_ori_get connect: " + url);

		String result = "";

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = httpclient.execute(httpGet);
			result = convertStreamToString(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "connect_simple_ori result: " + result);

		return result;
	}

	public static String connect_simple_ori(String url,
			List<NameValuePair> params) {
		Log.d(TAG, "connect_simple_ori connect: " + url);
		Log.d(TAG, "connect_simple_ori params: " + params);
		String result = "";

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			result = convertStreamToString(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "connect_simple_ori result: " + result);

		return result;
	}

	public static byte[] connect_simple_file_download(String url,
			List<NameValuePair> params) {
		Log.d(TAG, "connect_simplei_file_download connect: " + url);
		Log.d(TAG, "connect_simplei_file_download params: " + params);
		String result = "";

		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url);
		httpput.addHeader("User-agent", System.getProperty("http.agent"));
		httpput.addHeader("Content-Type", "application/x-www-form-urlencoded");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			for (int i = 0; i < params.size(); i++) {
				httpput.addHeader(params.get(i).getName(), params.get(i)
						.getValue());
			}
			httpput.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httpput);

			InputStream is = response.getEntity().getContent();

			byte[] buffer = new byte[1024];
			int size;
			while ((size = is.read(buffer)) > 0) {
				baos.write(buffer, 0, size);
			}

			result = new String(baos.toByteArray());
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		Log.d(TAG, "connect_simplei_file_download result: " + result);

		return baos.toByteArray();
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {

			// byte[] b1 = new byte[1024];
			// while ( is.read(b1) != -1)
			// sb.append(new String(b1));
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/*public static String checkStatus(Context context) {
		String url = SERVER + STATUS;
		ArrayList<NameValuePair> params = new ArrayList<>();

		params.add(new BasicNameValuePair("id", getDeviceId(context)));
		params.add(new BasicNameValuePair("type", "1"));
		params.add(new BasicNameValuePair("v", getVersion(context)));

		return connect_simple_ori(url, params);
	}*/

	public static String listPackage(Context context) {
		String url = SERVER + LIST;

		return connect_simple_ori_get(url);
	}

	public static String getPack(String code, Context context) {
		String url = SERVER + DOWNLOAD;
		ArrayList<NameValuePair> params = new ArrayList<>();

		params.add(new BasicNameValuePair("id", getDeviceId(context)));
		params.add(new BasicNameValuePair("code", code));

		return connect_simple_ori(url, params);
	}

	public static byte[] downloadPack(String url) {
		ArrayList<NameValuePair> params = new ArrayList<>();

		return connect_simple_file_download(url, params);
	}

	public static void downloadFromUrl(String DownloadUrl, String fileName) {

		try {
			File root = android.os.Environment.getExternalStorageDirectory();

			File dir = new File(root.getAbsolutePath() + "/lichaodai");
			if (dir.exists() == false) {
				dir.mkdirs();
			}

			URL url = new URL(DownloadUrl); // you can write here any link
			File file = new File(dir, fileName);

			long startTime = System.currentTimeMillis();
			Log.d("DownloadManager", "download begining");
			Log.d("DownloadManager", "download url:" + url);
			Log.d("DownloadManager", "downloaded file name:" + fileName);

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			Log.d("DownloadManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}

	}
	
	
	public static void downloadFromUrl2(String downloadUrl, String fileName) {
	    try {
	        File root = android.os.Environment.getExternalStorageDirectory();
	        File dir = new File(root.getAbsolutePath() + "/lichaodai");
	        if(dir.exists() == false){
	             dir.mkdirs();  
	        }

	        URL url = new URL(downloadUrl);
	        File file = new File(dir,fileName);

	        long startTime = System.currentTimeMillis();
	        Log.d("DownloadManager" , "download url:" +url);
	        Log.d("DownloadManager" , "download file name:" + fileName);

	        URLConnection uconn = url.openConnection();
	        //uconn.setReadTimeout(TIMEOUT_CONNECTION);
	        //uconn.setConnectTimeout(TIMEOUT_SOCKET);

	        InputStream is = uconn.getInputStream();
	        BufferedInputStream bufferinstream = new BufferedInputStream(is);

	        ByteArrayBuffer baf = new ByteArrayBuffer(5000);
	        int current = 0;
	        FileOutputStream fos = new FileOutputStream( file);
	        
	        while((current = bufferinstream.read()) != -1){
	            //baf.append((byte) current);
	            fos.write((byte) current);
	        }

	        
	        //fos.write(baf.toByteArray());
	        fos.flush();
	        fos.close();
	        Log.d("DownloadManager" , "download ready in" + ((System.currentTimeMillis() - startTime)/1000) + "sec");
	        int dotindex = fileName.lastIndexOf('.');
	        if(dotindex>=0){
	            fileName = fileName.substring(0,dotindex);
	        }
	    }

	    
	    catch(IOException e) {
	        Log.d("DownloadManager" , "Error:" + e);
	    }

	}
	
	private static String getDeviceId(Context context) {
		String id = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
		
		if (id == null || id.equals("")) {
			return "123456789";
		}
		return id;
	}
	
	private static String getVersion(Context context) {
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		if (pInfo == null) {
			return "v1.0";
		}
		
		String version = pInfo.versionName;
		return "v" + version;
	}
}
