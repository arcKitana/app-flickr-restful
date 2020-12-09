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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/flickrPhotos")
	public String getPhotos(Model model) {
	
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

		return AttributeNames.REDIRECT_PHOTOS;
	}

    @PostMapping(AttributeNames.FLICKR_PHOTOS)
    public String redirect(){
        log.info("============================================== Refresh Data ==============================================");
        return AttributeNames.REDIRECT_FLICKR_PHOTOS;
    }	
	
	@GetMapping("/photos")
    public String photos(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
    	
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(1);
		
    	Pageable firstPage = PageRequest.of((currentPage-1), pageSize, Sort.by(Order.asc("date")));
		
    	Page<FlickrRestfulObject> flickrRestfulObjectPage =  flickrRestfulObjectRepository.findAll(firstPage);

    	model.addAttribute("flickrRestfulObjectPage", flickrRestfulObjectPage);
    	
        int totalPages = flickrRestfulObjectPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    	
		return AttributeNames.PHOTOS;
    }

}
