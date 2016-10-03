package com.fsoc.wallpaper.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

	public void addAllEx(ArrayList<ImgPackObj> onlineList) {
        ArrayList<ImgPackObj> canAddList = new ArrayList<>();
        for (ImgPackObj onlineObj : onlineList) {
            if (!isExist(onlineObj)) {
                canAddList.add(onlineObj);
            }
        }
        this.addAll(canAddList);
	}

    /**
     * check exist id in currently list.
     * @param obj
     * @return
     */
    private boolean isExist(ImgPackObj obj) {
        for (ImgPackObj currentObj : this) {
            // if any object same id
            if (currentObj.getId().equals(obj.getId())) {
                return true;
            }
        }
        return false;
    }
}
