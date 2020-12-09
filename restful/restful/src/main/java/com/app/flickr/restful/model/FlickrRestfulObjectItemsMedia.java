package com.app.flickr.restful.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrRestfulObjectItemsMedia {

	private String m;

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	@Override
	public String toString() {
		return String.format("FlickrRestfulObjectItemsMedia [m=%s]", m);
	}
	
}
