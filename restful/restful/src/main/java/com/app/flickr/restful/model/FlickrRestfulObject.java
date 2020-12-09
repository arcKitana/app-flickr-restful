package com.app.flickr.restful.model;

import java.util.Arrays;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class FlickrRestfulObject {
	
    @Id
    private String id;	
	private String title;
	private String link;
	private String description;
	private Date modified;
	private String generator;
	private FlickrRestfulObjectItems[] items;
	private Date date;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	public FlickrRestfulObjectItems[] getItems() {
		return items;
	}
	public void setItems(FlickrRestfulObjectItems[] items) {
		this.items = items;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return String.format(
				"FlickrRestfulObject [id=%s, title=%s, link=%s, description=%s, modified=%s, generator=%s, items=%s, date=%s]",
				id, title, link, description, modified, generator, Arrays.toString(items), date);
	}
}
