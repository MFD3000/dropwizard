package com.helloworld.views;

import com.helloworld.core.Banner;
import com.yammer.dropwizard.views.View;

public class BannerView extends View {
    private Banner banner;
	public BannerView(Banner banner) {
		super("banner.mustache");
		this.banner = banner;
		// TODO Auto-generated constructor stub
	}
	public Banner getBanner() {
		return banner;
	}


}