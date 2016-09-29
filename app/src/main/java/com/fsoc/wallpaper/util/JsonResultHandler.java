package com.fsoc.wallpaper.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class JsonResultHandler {

	private int statusCode = 0;

	public JSONObject jObj = null;
	
	public static final String CODE = "code";
	public static final int CANNOT_CONNECT = -1;

	/**
	 *
	 * @param xml
     */
	public JsonResultHandler(String xml){
		if(xml == null) {
			return;
		}
		else {
			try {
				JSONObject jsonObject = XML.toJSONObject(xml);
				jObj = jsonObject.getJSONObject("root");
				init();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void init(){
        String tempStatusCode = jObj.optString(CODE, String.valueOf(CANNOT_CONNECT));
		try {
			statusCode = Integer.parseInt(tempStatusCode);
		}
		catch(NumberFormatException e) {
			statusCode = CANNOT_CONNECT;
		}
	}
	
	public boolean isOk() {
		return (statusCode == 1);
	}

	public JSONObject getjObj() {
		return jObj;
	}
	
	
}
