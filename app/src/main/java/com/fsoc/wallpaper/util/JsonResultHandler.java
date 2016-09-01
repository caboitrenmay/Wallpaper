package com.fsoc.wallpaper.util;

import org.json.JSONObject;

import android.util.Log;

public class JsonResultHandler {

	private int statusCode = 0;
	//private String statusMsg = null;
	
	public JSONObject jObj = null;
	
	public static final String CODE = "code";
	public static final int CANNOT_CONNECT = -1;
	
	public JsonResultHandler(String json){
		try{
			if(json == null){
				jObj = new JSONObject();
			}else{
				jObj = new JSONObject(json);
			}
			init();
		}catch(Exception e){
			Log.e("JsonResultHandler", "Constructor:e:"+e); 
			statusCode = CANNOT_CONNECT;
			//statusMsg = e.toString();
			//statusMsg = KryptoApplication.getInstance().getResources().getString(R.string.no_connection);
		}
	}
	
	private void init(){
        String tempStatusCode = jObj.optString(CODE, String.valueOf(CANNOT_CONNECT));
		try{
			statusCode = Integer.parseInt(tempStatusCode);
		}catch(Exception e){
			Log.e("JsonResultHandler", "init:e:"+e); 
			statusCode = CANNOT_CONNECT;
		}
		
		//statusMsg = jObj.optString(Constant.jsStatusMsg, "");
	}
	
	public boolean isOk() {
		return (statusCode == 1);
	}

	public JSONObject getjObj() {
		return jObj;
	}
	
	
}
