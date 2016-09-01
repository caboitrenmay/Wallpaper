package com.fsoc.wallpaper.model;

public class ImgPackObj {

	private String id;
	private String name;
	private String subname;
	
	private String thumb;
	private String sms_content;
	private String sms_to;
	
	private String pack;

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

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getSms_content() {
		return sms_content;
	}

	public void setSms_content(String sms_content) {
		this.sms_content = sms_content;
	}

	public String getSms_to() {
		return sms_to;
	}

	public void setSms_to(String sms_to) {
		this.sms_to = sms_to;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	
}
