package com.app.flickr.restful.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrRestfulObjectItems {

	private String title;
	private String link;
	private FlickrRestfulObjectItemsMedia media;
	private Date dateTaken;
	private String description;
	private Date published;
	private String author;
	private String author_Id;
	private String tags;
	
	
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
	public FlickrRestfulObjectItemsMedia getMedia() {
		return media;
	}
	public void setMedia(FlickrRestfulObjectItemsMedia media) {
		this.media = media;
	}
	public Date getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor_Id() {
		return author_Id;
	}
	public void setAuthor_Id(String author_Id) {
		this.author_Id = author_Id;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	@Override
	public String toString() {
		return String.format(
				"FlickrRestfulObjectItems [title=%s, link=%s, media=%s, dateTaken=%s, description=%s, published=%s, author=%s, authorId=%s, tags=%s]",
				title, link, media, dateTaken, description, published, author, author_Id, tags);
	}
}
