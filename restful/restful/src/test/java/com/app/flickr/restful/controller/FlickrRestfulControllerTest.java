package com.app.flickr.restful.controller;

import com.app.flickr.restful.model.FlickrRestfulObject;
import com.app.flickr.restful.repository.FlickrRestfulObjectRepository;
import com.app.flickr.restful.util.AttributeNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ExtendedModelMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class FlickrRestfulControllerTest {

    @InjectMocks
    FlickrRestfulController flickrRestfulController;

    @Mock
    FlickrRestfulObjectRepository flickrRestfulObjectRepository;

    @BeforeEach
    void SetUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPhotosFromFlickr() {
        String result = flickrRestfulController.getPhotosFromFlickr(new ExtendedModelMap());
        assertEquals(AttributeNames.REDIRECT_PHOTOS, result);
    }

    @Test
    void callFlickrPhoto() {
        String result = flickrRestfulController.callFlickrPhoto("");
        assertNotNull(result);
    }

    @Test
    void photos() {

        Page<FlickrRestfulObject> flickrRestfulObjectPage = Mockito.mock(Page.class);
        Pageable firstPage = PageRequest.of(0, 1, Sort.by(Sort.Order.asc("date")));
        when(flickrRestfulObjectRepository.findAll(firstPage)).thenReturn( flickrRestfulObjectPage );

        String result = flickrRestfulController.photos(new ExtendedModelMap(), Optional.of(1), Optional.of(1));
        assertEquals("photos", result);
    }

    @Test
    void search() {
        Page<FlickrRestfulObject> flickrRestfulObjectPage = Mockito.mock(Page.class);
        Pageable firstPage = PageRequest.of(0, 1, Sort.by(Sort.Order.asc("date")));
        when(flickrRestfulObjectRepository.findAll(firstPage)).thenReturn( flickrRestfulObjectPage );

        String result = flickrRestfulController.search(new ExtendedModelMap(), "test", Optional.of(1), Optional.of(1));
        assertEquals("photos", result);
    }

    @Test
    void deleteAllPhotos() {
        String result = flickrRestfulController.deleteAllPhotos();
        assertEquals(AttributeNames.REDIRECT_PHOTOS, result);
    }

    @Test
    void epDeleteAllPhotos() {
        String result = flickrRestfulController.epDeleteAllPhotos();
        assertEquals(AttributeNames.REDIRECT_PHOTOS, result);
    }
}