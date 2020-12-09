package com.app.flickr.restful.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.flickr.restful.model.FlickrRestfulObject;

@Repository
public interface FlickrRestfulObjectRepository extends MongoRepository<FlickrRestfulObject, String> {

	
}
