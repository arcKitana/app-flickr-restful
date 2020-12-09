package com.app.flickr.restful.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.flickr.restful.model.FlickrRestfulObject;
import com.app.flickr.restful.model.FlickrRestfulObjectItems;
import com.app.flickr.restful.repository.FlickrRestfulObjectItemsRepository;
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

	@Autowired
	private FlickrRestfulObjectItemsRepository flickrRestfulObjectItemsRepository;
	
	@GetMapping("/flickrPhotos")
	public String getPhotosFromFlickr(Model model) {

		log.info(">>>>>   getPhotosFromFlickr method is being called   ==============================================");
			
		savePhotoFromFickr(callFlickrPhoto(""));
    
		return AttributeNames.REDIRECT_PHOTOS;
	}
	
	
	public String callFlickrPhoto(String criteria) {
		
		String concatString = "";
		
		try {
			log.info(">>>>>   callFlickrPhoto method is being called   ==============================================");
	
	
			// Get data from Flickr API
			log.info(">>>>>   Get data from Flickr");
	
			String urlSite = ApplicationConstants.URL_FLICKR_SITE;
			
			urlSite = urlSite + "&tags=" + criteria + "&title=" + criteria + "&description=" + criteria;
			
			URL url = new URL(urlSite);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			if(conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error Code >> " + conn.getResponseCode());
			}
	
			log.info(">>>>>   Data from server Flickr");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String input;
			
			
			log.info(">>>>>   Concat data Flickr in JSON format");
			while ((input = br.readLine()) != null) {
				concatString = concatString + input;
			}
			
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
		
		return concatString;
	}
	
	public void savePhotoFromFickr(String concatString) {
		
		try {
			log.info(">>>>>   savePhotoFromFickr method is being called   ==============================================");
			
			// Convert JSON
			ObjectMapper mapper = new ObjectMapper();
			FlickrRestfulObject flickrRestfulObject = mapper.readValue(concatString, FlickrRestfulObject.class);
			
			flickrRestfulObject.setDate(new java.util.Date());
			log.info(">>>>>   set Date : {}", flickrRestfulObject.getDate());
	
			flickrRestfulObjectRepository.save(flickrRestfulObject);
			log.info(">>>>>   Insert Data successfully");
			
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    catch (Exception exc){
	        exc.printStackTrace();
	    }
	}
	

    @PostMapping(AttributeNames.FLICKR_PHOTOS)
    public String redirect(){
    	log.info(">>>>>   redirect method is being called   ==============================================");
        return AttributeNames.REDIRECT_FLICKR_PHOTOS;
    }	
    
	
	@GetMapping("/photos")
    public String photos(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
    	
		log.info(">>>>>   photos method is being called   ==============================================");
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(1);
		
		log.info(">>>>>   Page {} Size {}", currentPage, pageSize);
		
    	Pageable firstPage = PageRequest.of((currentPage-1), pageSize, Sort.by(Order.asc("date")));
		
    	Page<FlickrRestfulObject> flickrRestfulObjectPage =  flickrRestfulObjectRepository.findAll(firstPage);

    	FlickrRestfulObjectItems[] flickrRestfulObjectItems = null;
    	
    	if(flickrRestfulObjectPage.getContent().size() != 0)
    		flickrRestfulObjectItems = (flickrRestfulObjectPage.getContent().get(0)).getItems();
    	    	
    	model.addAttribute("flickrRestfulObjectItems", flickrRestfulObjectItems);
    	model.addAttribute("flickrRestfulObjectPage", flickrRestfulObjectPage);
    	model.addAttribute("page", "Page : ");
    	
    	int totalPages = flickrRestfulObjectPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		log.info(">>>>>   totalPages {}", totalPages);
    	
		return AttributeNames.PHOTOS;
    }

	
	@PostMapping("/search")
	public String search(Model model, @RequestParam(value="title") String title, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
		
		log.info(">>>>>   search method is being called   ==============================================");
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(1);
		
		log.info(">>>>>   Page {} Size {}", currentPage, pageSize);
		log.info(">>>>>   Title : {} ", title);
		
    	Pageable firstPage = PageRequest.of((currentPage-1), pageSize, Sort.by(Order.asc("date")));
    	
    	int totalPages;
    	
     		savePhotoFromFickr(callFlickrPhoto(title));
     		
        	Page<FlickrRestfulObject> flickrRestfulObjectPage =  flickrRestfulObjectRepository.findAll(firstPage);
        	
        	FlickrRestfulObjectItems[] flickrRestfulObjectItems = null;
        	
        	if(flickrRestfulObjectPage.getContent().size() != 0)
        		flickrRestfulObjectItems = (flickrRestfulObjectPage.getContent().get(0)).getItems();
        	
            model.addAttribute("messageError", "Record Not Found");
        	model.addAttribute("flickrRestfulObjectItems", flickrRestfulObjectItems);
        	model.addAttribute("flickrRestfulObjectPage", flickrRestfulObjectPage);
        	
        	totalPages = flickrRestfulObjectPage.getTotalPages();
    	
		
    	model.addAttribute("page", "Page : ");
    	
    	if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		log.info(">>>>>   totalPages {}", totalPages);
		
		return AttributeNames.PHOTOS;
	}
	
	
	@PostMapping("/photos")
	public String deleteAllPhotos() {
		
		log.info(">>>>>   deleteAllPhotos method is being called   ==============================================");
		
		flickrRestfulObjectRepository.deleteAll();
		
		return AttributeNames.REDIRECT_PHOTOS;
	}
	
	
	@DeleteMapping("/photos")
	public String epDeleteAllPhotos() {
		
		log.info(">>>>>   epDeleteAllPhotos method is being called   ==============================================");
		
		flickrRestfulObjectRepository.deleteAll();
		
		return AttributeNames.REDIRECT_PHOTOS;
	}	
	
}
