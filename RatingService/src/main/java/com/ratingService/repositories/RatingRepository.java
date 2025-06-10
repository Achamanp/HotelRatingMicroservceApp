package com.ratingService.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratingService.entites.Rating;


@Repository
public interface RatingRepository extends JpaRepository<Rating, String>{

	List<Rating> findByUserId(String userId);

	List<Rating> findByHotelId(String hotelId);

}
