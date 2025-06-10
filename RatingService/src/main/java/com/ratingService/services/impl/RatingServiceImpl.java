package com.ratingService.services.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ratingService.entites.Rating;
import com.ratingService.repositories.RatingRepository;
import com.ratingService.services.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingByUserId(String userId) {
        return ratingRepository.findByUserId(userId);
    }

    @Override
    public List<Rating> getRatingByHotelId(String hotelId) {
        return ratingRepository.findByHotelId(hotelId);
    }

    @Override
    public Rating getRatingById(String id) {
        return ratingRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Rating not found with "+ id));
    }

    @Override
    public void deleteRating(String id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with "+ id));
        ratingRepository.delete(rating);
    }

    @Override
    public Rating updateRating(Rating rating) {
        Rating existingRating = ratingRepository.findById(rating.getRatingId())
                .orElseThrow(() -> new RuntimeException("Rating not found with id " + rating.getRatingId()));

        existingRating.setRating(rating.getRating());
        existingRating.setFeedback(rating.getFeedback());
        existingRating.setHotelId(rating.getHotelId());
        existingRating.setUserId(rating.getUserId());

        return ratingRepository.save(existingRating);
    }

}
