package com.fsoc.wallpaper.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ImgPackList extends ArrayList<ImgPackObj> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ImgPackList() {
		super();
	}
	
	public ImgPackList(final List<ImgPackObj> listPacks) {
		super();
		addAll(listPacks);
	}

	public static ImgPackList initFromJsonArray(String jsonArray) {
		Gson gson = new Gson();
		return gson.fromJson(jsonArray, ImgPackList.class);
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
