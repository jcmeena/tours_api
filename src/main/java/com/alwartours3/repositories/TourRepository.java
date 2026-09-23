package com.alwartours3.repositories;




import com.alwartours3.entities.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface TourRepository extends PagingAndSortingRepository<Tour, Integer> {
    Page<Tour> findByTourPackageCode(String code , Pageable pageable);

    @RestResource(exported = false)
    @Override
    <S extends Tour> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends Tour> Iterable<S> saveAll(Iterable<S> entities);

    @RestResource(exported = false)
    @Override
    void deleteById(Integer integer);

    @RestResource(exported = false)
    @Override
    void delete(Tour entity);
//
//    @RestResource(exported = false)
//    @Override
//    void deleteAllById(Iterable<? extends Integer> integers);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends Tour> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();
}
