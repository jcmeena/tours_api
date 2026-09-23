package com.alwartours3.web;

import com.alwartours3.services.TourRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    private TourRatingService tourRatingService;


    public String return400(NoSuchElementException e){
        LOGGER.error("Unable to complete transaction" , e);
        return e.getMessage();

    }
}
