package com.app.flickr.restful.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.flickr.restful.model.FlickrRestfulObject;
import com.app.flickr.restful.repository.FlickrRestfulObjectRepository;
import com.app.flickr.restful.util.ApplicationConstants;
import com.app.flickr.restful.util.AttributeNames;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class FlickrRestfulController {
	
	// Fields
	private static final Logger log = LoggerFactory.getLogger(FlickrRestfulController.class);
	
	@Autowired
	private FlickrRestfulObjectRepository flickrRestfulObjectRepository;
	
	@GetMapping(AttributeNames.PHOTOS)
	public String displayPhotos(Model model) {
	
		try {
			
			log.info(">>>>>   Display method is called");


			// Get data from Flickr API
			log.info(">>>>>   Get data from Flickr");

			String urlSite = ApplicationConstants.URL_FLICKR_SITE;
			URL url = new URL(urlSite);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");

			
			if(conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error Code >> " + conn.getResponseCode());
			}

			log.info(">>>>>   Data from server Flickr");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String input;
			String concatString = "";
			
			log.info(">>>>>   Concat data Flickr in JSON format");
			while ((input = br.readLine()) != null) {
				concatString = concatString + input;
			}
			
			
			// Convert JSON
			ObjectMapper mapper = new ObjectMapper();
			FlickrRestfulObject flickrRestfulObject = mapper.readValue(concatString, FlickrRestfulObject.class);
			
			
			
			// Mapping into attributes
			model.addAttribute(AttributeNames.OBJECT_TITLE, flickrRestfulObject.getTitle());
			model.addAttribute(AttributeNames.OBJECT_LINK, flickrRestfulObject.getLink());   
			model.addAttribute(AttributeNames.OBJECT_DESCRIPTION, flickrRestfulObject.getDescription());
			model.addAttribute(AttributeNames.OBJECT_MODIFIED, flickrRestfulObject.getModified());
			model.addAttribute(AttributeNames.OBJECT_GENERATOR, flickrRestfulObject.getGenerator());
			
			log.info(">>>>>   Object Title {} ", flickrRestfulObject);						
						
			for(int i=0; i<flickrRestfulObject.getItems().length; i++) {
				model.addAttribute(AttributeNames.OBJECT_ITEMS_TITLE + (i+1), flickrRestfulObject.getItems()[i].getTitle());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_LINK + (i+1), flickrRestfulObject.getItems()[i].getLink());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_MEDIA + (i+1), flickrRestfulObject.getItems()[i].getMedia().getM());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_DATE_TAKEN + (i+1), flickrRestfulObject.getItems()[i].getDateTaken());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_DESCRIPTION + (i+1), flickrRestfulObject.getItems()[i].getDescription());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_PUBLISHED + (i+1), flickrRestfulObject.getItems()[i].getPublished());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_AUTHOR + (i+1), flickrRestfulObject.getItems()[i].getAuthor());
				model.addAttribute(AttributeNames.OBJECT_ITEMS_AUTHOR_ID + (i+1), flickrRestfulObject.getItems()[i].getAuthor_Id());  
				model.addAttribute(AttributeNames.OBJECT_ITEMS_TAGS + (i+1), flickrRestfulObject.getItems()[i].getTags());	

				log.info(">>>>>   ObjectItems  {} ", flickrRestfulObject.getItems()[i]);				
			}
			

			
			flickrRestfulObject.setDate(new java.util.Date());
			log.info(">>>>>   set Date : {}", flickrRestfulObject.getDate());
	

			flickrRestfulObjectRepository.save(flickrRestfulObject);
			
			log.info(">>>>>   Insert Data successfully");

			conn.disconnect();

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
		
		
		return AttributeNames.PHOTOS;
	}
	
	
    @PostMapping(AttributeNames.PHOTOS)
    public String processingDisplay(){
        log.info("============================================== Refresh Data ==============================================");
        return AttributeNames.REDIRECT_PHOTOS;
    }
}
