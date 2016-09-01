package com.fsoc.wallpaper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsPreference {

	SharedPreferences prefs;
	Editor prefsEditor;
	private final String ITEM_DOWNLOADED = "ITEM_DOWNLOADED";
	private final String ID_LIST = "ID_LIST";
	
	private final String ID_PACK = "ID_PACK";
	
	public SettingsPreference(Context context){
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefsEditor = prefs.edit();
		// then you use
	}
	private static SettingsPreference instance;

	public static SettingsPreference getInstance(Context context) {
		if (instance == null) {
			synchronized (SettingsPreference.class) {
				if (instance == null) {
					instance = new SettingsPreference(context.getApplicationContext());
				}
			}
		}
		return instance;
	}

	public static SettingsPreference getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					"You have to call getInstance with context to init preference first!");
		} else {
			return instance;
		}
	}
	
	public void setItemDownloaded(String itemDownloaded) {
		String current = prefs.getString(ITEM_DOWNLOADED, "");
		String result;
		if (current.equals("")) {
			result = current + itemDownloaded;
		}
		else {
			result = current + "," + itemDownloaded;
		}
		
        prefsEditor.putString(ITEM_DOWNLOADED, result);
        prefsEditor.commit();
    }

    public String getItemDownloaded() {
        return prefs.getString(ITEM_DOWNLOADED, "");
	}
    
    public void setIdList(String json) {
    	prefsEditor.putString(ID_LIST, json);
        prefsEditor.commit();
	}
    
    public String getIdList() {
        return prefs.getString(ID_LIST, "");
	}
    
    public void setIdPack(String pack) {
    	prefsEditor.putString(ID_PACK, pack);
        prefsEditor.commit();
	}
    
    public String getIdPack() {
        return prefs.getString(ID_PACK, "1");
	}
}
