package com.alwartours3;

import com.alwartours3.entities.Tour;
import com.alwartours3.entities.TourRating;
import com.alwartours3.repositories.TourRatingRepository;
import com.alwartours3.repositories.TourRepository;
import com.alwartours3.services.TourRatingService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is; // static import of hamcrest
import static org.hamcrest.MatcherAssert.assertThat; // static import of hamcrest
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TourRatingServiceTest {

    private static final Integer TOUR_ID = 1;
    private static final Integer CUSTOMER_ID = 123;
    private static final Integer TOUR_RATING_ID = 100;
    @Mock
    private TourRepository tourRepositoryMock;
    @Mock
    private TourRatingRepository tourRatingRepositoryMock;

    @InjectMocks
    private TourRatingService tourRatingService;

    @Mock
    private Tour tourMock;

    @Mock
    private TourRating tourRatingMock;

    @Before
    public void setupReturnValuesOfMockMethods(){
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tourMock));
        when(tourMock.getId()).thenReturn(TOUR_ID);

        when(tourRatingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID,CUSTOMER_ID)).thenReturn(Optional.of(tourRatingMock));
        when(tourRatingRepositoryMock.findByTourId(TOUR_ID)).thenReturn(Arrays.asList(tourRatingMock));
    }

    /***
     * verify return values
     */
    @Test
    public void lookupRatingById(){
        when(tourRatingRepositoryMock.findById(TOUR_RATING_ID)).thenReturn(Optional.of(tourRatingMock));
        MatcherAssert.assertThat(tourRatingService.lookupRatingById(TOUR_RATING_ID).get(), CoreMatchers.is(tourRatingMock));
    }
    @Test
    public void lookupAll() {
        when(tourRatingRepositoryMock.findAll()).thenReturn(Arrays.asList(tourRatingMock));
        assertThat(tourRatingService.lookupAll().get(0),is(tourRatingMock));
    }

    @Test
    public void getAverageScore() {
        when(tourRatingMock.getScore()).thenReturn(10);
        assertThat(tourRatingService.getAverageScore(TOUR_ID), is(10.0));
    }

    @Test
    public void lookupRatings() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);
        when(tourRatingRepositoryMock.findByTourId(TOUR_ID,pageable)).thenReturn(page);

        assertThat(tourRatingService.lookupRatings(TOUR_ID, pageable), is(page));
    }
    /**
     * verify invocation of dependencies
     *
     */
    @Test
    public void delete() {
        tourRatingService.delete(TOUR_ID,CUSTOMER_ID);
        verify(tourRatingRepositoryMock).delete(any(TourRating.class));
    }

    @Test
    public void rateMany() {
        tourRatingService.rateMany(TOUR_ID,10 , new Integer[]{CUSTOMER_ID ,CUSTOMER_ID+1});
        verify(tourRatingRepositoryMock,times(2)).save(any(TourRating.class));
    }
    @Test
    public void update() {
        tourRatingService.update(TOUR_ID,CUSTOMER_ID,10,"TEST");
        verify(tourRatingRepositoryMock).save(any(TourRating.class));

        verify(tourRatingMock).setComment("TEST");
        verify(tourRatingMock).setScore(10);
    }
    @Test
    public void updateSome() {
        tourRatingService.updateSome(TOUR_ID,CUSTOMER_ID,9,"TEST2");
        verify(tourRatingRepositoryMock).save(any(TourRating.class));
        verify(tourRatingMock).setComment("TEST2");
        verify(tourRatingMock).setScore(9);
    }

    /**
     * verify invocations of dependencies
     * Capture parameter values
     * verify the parameters
     *
     */

    @Test
    public void createNew() {
        ArgumentCaptor<TourRating> argumentCaptor = ArgumentCaptor.forClass(TourRating.class);

        tourRatingService.createNew(TOUR_ID,CUSTOMER_ID,2,"OK");
        //verify(tourRatingRepositoryMock).save(any(TourRating.class));
        verify(tourRatingRepositoryMock).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getTour(), is(tourMock));
        assertThat(argumentCaptor.getValue().getCustomerId(), is(CUSTOMER_ID));
        assertThat(argumentCaptor.getValue().getScore(), is(2));
        assertThat(argumentCaptor.getValue().getComment(), is("OK"));

    }
}
