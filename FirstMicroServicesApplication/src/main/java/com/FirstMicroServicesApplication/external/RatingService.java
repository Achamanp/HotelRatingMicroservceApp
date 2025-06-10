package com.FirstMicroServicesApplication.external;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.FirstMicroServicesApplication.entities.Rating;

@EnableFeignClients
public interface RatingService {
	
	
	@PostMapping("/api/ratings")
	Rating createRating(Rating rating);
	
	@PutMapping("/api/ratings/{ratingId}")
	Rating updateRating(@PathVariable("ratingId") String ratingId,Rating rating);
	
	
	@DeleteMapping("/api/ratings/{ratingId}")
	void deleteRating(@PathVariable String ratingId);
	
	
	   

}
