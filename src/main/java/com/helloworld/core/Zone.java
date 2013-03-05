package com.helloworld.core;

import java.util.List;

public class Zone {

	
	private int id;
	private List<Banner> banners;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Banner> getBanners() {
		return banners;
	}
	public void setBanners(List<Banner> banners) {
		this.banners = banners;
	}
	
	
}
