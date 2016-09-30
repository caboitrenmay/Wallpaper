package com.fsoc.wallpaper.model;

public class ImgPackObj {

	private String id;
	private String name;

	private String thumb;
	private String pack;

	public ImgPackObj() {
	}

	public ImgPackObj(String name, String thumb) {
		this.name = name;
		this.thumb = thumb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	
}
