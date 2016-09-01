package com.fsoc.wallpaper.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class IdPackList extends ArrayList<IdPackObj> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public IdPackList() {
		super();
	}
	
	public IdPackList(final List<IdPackObj> listPacks) {
		super();
		addAll(listPacks);
	}

	public static IdPackList initFromJsonArray(String jsonArray) {
		Gson gson = new Gson();
		return gson.fromJson(jsonArray, IdPackList.class);
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
