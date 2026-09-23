package com.alwartours3.repositories;

import com.alwartours3.entities.TourRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends CrudRepository<TourRating, Integer> {

    Optional<TourRating> findByTourIdAndCustomerId(Integer tourId, Integer customerId);
    List<TourRating> findByTourId(Integer tourId);
    Page<TourRating> findByTourId(Integer tourId, Pageable pageable);


}
