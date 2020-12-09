package com.app.flickr.restful.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.flickr.restful.model.FlickrRestfulObjectItems;

@Repository
public interface FlickrRestfulObjectItemsRepository extends MongoRepository<FlickrRestfulObjectItems, String> {

	
	Page<FlickrRestfulObjectItems> findByTitleLike(Pageable pageable, String title);
	
}
