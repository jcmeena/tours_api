package com.alwartours3.web;

import com.alwartours3.entities.TourRating;
import com.alwartours3.services.TourRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "tours/{tourId}/ratings")
public class TourRatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TourRatingController.class);
    private TourRatingService tourRatingService;
//    private TourRatingRepository tourRatingRepository ;
//    private TourRepository tourRepository;
//
//    @Autowired
//    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
//        this.tourRatingRepository = tourRatingRepository;
//        this.tourRepository = tourRepository;
//    }
    @Autowired
    public TourRatingController(TourRatingService tourRatingService){
        System.out.println("#### autowiring done ####");
        this.tourRatingService = tourRatingService;
    }
    protected TourRatingController(){

    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId,
                                 @RequestBody @Validated RatingDto ratingDto){
        tourRatingService.createNew(tourId,ratingDto.getCustomerId(),ratingDto.getScore(), ratingDto.getComment());
        //Tour tour  = verifyTour(tourId);

//        tourRatingRepository.save(new TourRating(
//                new TourRatingPk(tour,ratingDto.getCustomerId()),
//                ratingDto.getScore(), ratingDto.getComment()));
    }

//    @GetMapping
//    public List<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId){
//        return tourRatingRepository.findByPkTourId(tourId).stream().map(RatingDto::new).collect(Collectors.toList());
//    }
    @PostMapping("/{score}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createManyTourRatings(@PathVariable(value = "tourId") int tourId,
                                      @PathVariable(value = "score") int score,
                                      @RequestParam("customers") Integer customers[]) {
        LOGGER.info("called POST /tours/{}/ratings/{}", tourId,score);
        tourRatingService.rateMany(tourId, score, customers);
    }
    @GetMapping
    public Page<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId, Pageable pageable){

        Page<TourRating> tourRatingPage= tourRatingService.lookupRatings(tourId,pageable);
        List<RatingDto> ratingDtoList = tourRatingPage.getContent().stream().map(
                tourRating -> toDto(tourRating)
        ).collect(Collectors.toList());
        return new PageImpl<RatingDto>(ratingDtoList, pageable, tourRatingPage.getTotalPages());
//        Page<TourRating> ratings = tourRatingRepository.findByPkTourId(tourId,pageable);
//        return new PageImpl<>(
//                ratings.get().map(RatingDto::new).collect(Collectors.toList()),
//                pageable,
//                ratings.getTotalElements()
//        );
    }
    @GetMapping(path = "/average")
    AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable(value = "tourId") int tourId){
    //Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourId){
        return new AbstractMap.SimpleEntry<String, Double>("average", tourRatingService.getAverageScore(tourId));

//        verifyTour(tourId);
//        return Map.of("average",
//                tourRatingRepository.findByPkTourId(tourId).stream()
//                        .mapToInt(TourRating::getScore).average()
//                        .orElseThrow(()-> new NoSuchElementException("No such tour exists")));
    }

    @PutMapping
    public RatingDto updateWithPut(@PathVariable(value = "tourId") int tourId,
                                   @RequestBody @Validated RatingDto ratingDto){
        return toDto(tourRatingService.update(tourId, ratingDto.getCustomerId(),
                ratingDto.getScore(), ratingDto.getComment()));
        //        TourRating tourRating = verifyTourRating(tourId, ratingDto.getCustomerId());
//        tourRating.setScore(ratingDto.getScore());
//        tourRating.setComment(ratingDto.getComment());
//        return new RatingDto(tourRatingRepository.save(tourRating));
    }
    @PatchMapping
    public RatingDto updateWithPatch(@PathVariable(value = "tourId") int tourId,
                                     @RequestBody @Validated RatingDto ratingDto){
        return toDto(tourRatingService.updateSome(tourId, ratingDto.getCustomerId(),
                ratingDto.getScore(), ratingDto.getComment()));
//        TourRating tourRating = verifyTourRating(tourId, ratingDto.getCustomerId());
//        if(ratingDto.getScore() != null){
//            tourRating.setScore(ratingDto.getScore());
//        }
//        if(ratingDto.getComment() != null){
//            tourRating.setComment(ratingDto.getComment());
//        }
//        return new RatingDto(tourRatingRepository.save(tourRating));
    }

    @DeleteMapping(path = "/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId,
                       @PathVariable(value = "customerId") int customerId){
//        TourRating tourRating = verifyTourRating(tourId,customerId);
//        tourRatingRepository.delete(tourRating);
        tourRatingService.delete(tourId, customerId);
    }
//    private Tour verifyTour(Integer tourId){
//        return tourRepository.findById(tourId)
//                .orElseThrow(()->  new NoSuchElementException("Tour does not exist "+tourId));
//    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String return400(NoSuchElementException ex){
        return ex.getMessage();
    }

//    private TourRating verifyTourRating(int tourId, int customerId){
//        return tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId,customerId)
//                .orElseThrow(()-> new NoSuchElementException("Tour rating pair for tourId "+ tourId +" and customerId "+customerId +"does not exist"));
//    }

    private RatingDto toDto(TourRating tourRating) {
        return new RatingDto(tourRating.getScore(), tourRating.getComment(), tourRating.getCustomerId());
    }
}
