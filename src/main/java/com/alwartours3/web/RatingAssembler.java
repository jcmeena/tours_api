package com.alwartours3.web;

import com.alwartours3.entities.TourRating;
import com.alwartours3.repositories.TourRepository;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;



public class RatingAssembler extends RepresentationModelAssemblerSupport<TourRating,RatingDto> {

    //Helper to fetch Spring Data Rest Repository links.
    private RepositoryEntityLinks entityLinks;

    public RatingAssembler(Class<?> controllerClass, Class<RatingDto> resourceType) {
        super(controllerClass, resourceType);
    }

//    public RatingAssembler( RepositoryEntityLinks entityLinks) {
//        super(RatingController.class, RatingDto.class);
//        this.entityLinks = entityLinks;
//    }

    @Override
    public RatingDto toModel(TourRating tourRating) {
        RatingDto rating = new RatingDto(tourRating.getScore(), tourRating.getComment(), tourRating.getCustomerId());

//        // "self" : ".../ratings/{ratingId}"
//        WebMvcLinkBuilder ratingLink = linkTo(methodOn(RatingController.class).getRating(tourRating.getId()));
//        rating.add(ratingLink.withSelfRel());
//
//        //"tour" : ".../tours/{tourId}"
//        Link tourLink = entityLinks.linkToSingleResource(TourRepository.class, tourRating.getTour().getId());
//        rating.add(tourLink.withRel("tour"));
        return rating;
    }
}
