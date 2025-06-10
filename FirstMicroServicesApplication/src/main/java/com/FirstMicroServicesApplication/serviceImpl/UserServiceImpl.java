package com.FirstMicroServicesApplication.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.FirstMicroServicesApplication.entities.Hotel;
import com.FirstMicroServicesApplication.entities.Rating;
import com.FirstMicroServicesApplication.entities.User;
import com.FirstMicroServicesApplication.exception.UserNotFoundException;
import com.FirstMicroServicesApplication.external.HotelService;
import com.FirstMicroServicesApplication.repository.UserRepository;
import com.FirstMicroServicesApplication.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private HotelService hotelService;
    
    @Value("${microservices.rating.url:http://RATINGSERVICE}")
    private String ratingServiceUrl;
    
    @Value("${microservices.hotel.url:http://USERSERVICEAPPLICATION}")
    private String hotelServiceUrl;

    @Override
    public User saveUser(User user) {
        logger.info("Attempting to save user with email: {}", user.getEmail());
        
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("User with email {} already exists", user.getEmail());
            throw new RuntimeException("User with email already exists");
        }
        
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        
        User savedUser = userRepository.save(user);
        logger.info("Successfully saved user with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    public List<User> getAllUser() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        
        if (users.isEmpty()) {
            logger.info("No users found");
            return users;
        }
        
        // Fetch ratings with hotel details for each user
        for (User user : users) {
            try {
                List<Rating> ratings = fetchUserRatings(user.getUserId());
                
                // Enrich each rating with hotel information (same as getUserById)
                List<Rating> ratingsWithHotels = ratings.stream().map(rating -> {
                    try {
//                        ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
//                            hotelServiceUrl + "/api/hotels/" + rating.getHotelId(), 
//                            Hotel.class
//                        );
                        Hotel hotel = hotelService.getHotel(rating.getHotelId());
                        rating.setHotel(hotel);
                        return rating;
                    } catch (RestClientException e) {
                        logger.error("Failed to fetch hotel details for rating {}: {}", 
                            rating.getRatingId(), e.getMessage());
                        return rating; // Return rating without hotel info
                    }
                }).collect(Collectors.toList());
                
                user.setRatings(ratingsWithHotels);
                
            } catch (Exception e) {
                logger.error("Failed to fetch ratings for user {}: {}", user.getUserId(), e.getMessage());
                user.setRatings(new ArrayList<>());
            }
        }
        
        logger.info("Successfully fetched {} users with their ratings and hotel details", users.size());
        return users;
    }

    @Override
    public User updateUser(User user, String userId) throws UserNotFoundException {
        logger.info("Updating user with ID: {}", userId);
        
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", userId);
                return new UserNotFoundException("User not found with id " + userId);
            });

        // Update only non-null fields
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAbout() != null) {
            existingUser.setAbout(user.getAbout());
        }

        User updatedUser = userRepository.save(existingUser);
        logger.info("Successfully updated user with ID: {}", userId);
        return updatedUser;
    }

    @Override
    public void deleteUser(String userId) throws UserNotFoundException {
        logger.info("Deleting user with ID: {}", userId);
        
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", userId);
                return new UserNotFoundException("User not found with id " + userId);
            });

        userRepository.delete(existingUser);
        logger.info("Successfully deleted user with ID: {}", userId);
    }

    @Override
    public User getUserById(String userId) throws UserNotFoundException {
        logger.info("Fetching user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", userId);
                return new UserNotFoundException("User not found with id " + userId);
            });

        // Fetch ratings with hotel details (same logic as getAllUser)
        List<Rating> ratings = fetchUserRatings(userId);
        List<Rating> ratingsWithHotels = ratings.stream().map(rating -> {
            try {
//                ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
//                    hotelServiceUrl + "/api/hotels/" + rating.getHotelId(), 
//                    Hotel.class
   //             );
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
                return rating;
            } catch (RestClientException e) {
                logger.error("Failed to fetch hotel details for rating {}: {}", 
                    rating.getRatingId(), e.getMessage());
                return rating; // Return rating without hotel info
            }
        }).collect(Collectors.toList());
        
        user.setRatings(ratingsWithHotels);
        
        logger.info("Successfully fetched user {} with {} ratings", userId, ratingsWithHotels.size());
        return user;
    }

    /**
     * Fetches user ratings and enriches them with hotel information
     */
    private List<Rating> fetchUserRatingsWithHotels(String userId) {
        List<Rating> ratings = fetchUserRatings(userId);
        
        if (ratings.isEmpty()) {
            return ratings;
        }
        
        // Enrich ratings with hotel information
        return ratings.stream()
            .map(this::enrichRatingWithHotel)
            .collect(Collectors.toList());
    }

    /**
     * Fetches ratings for a specific user
     */
    private List<Rating> fetchUserRatings(String userId) {
        try {
            String url = ratingServiceUrl + "/api/ratings/user/" + userId;
            logger.debug("Fetching ratings from URL: {}", url);
            
            ResponseEntity<Rating[]> response = restTemplate.getForEntity(url, Rating[].class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Rating> ratings = List.of(response.getBody());
                logger.info("Successfully fetched {} ratings for user {}", ratings.size(), userId);
                return ratings;
            } else {
                logger.warn("No ratings found for user {} - Response status: {}", userId, response.getStatusCode());
                return new ArrayList<>();
            }
            
        } catch (RestClientException e) {
            logger.error("Failed to fetch ratings for user {}: {}", userId, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Enriches a rating with hotel information
     */
    private Rating enrichRatingWithHotel(Rating rating) {
        try {
            String hotelId = rating.getHotelId();
            if (hotelId == null || hotelId.trim().isEmpty()) {
                logger.warn("Rating {} has no hotel ID", rating.getRatingId());
                return rating;
            }
            
//            String url = hotelServiceUrl + "/api/hotels/" + hotelId;
//            logger.debug("Fetching hotel details from URL: {}", url);
//            
//            ResponseEntity<Hotel> response = restTemplate.getForEntity(url, Hotel.class);
            
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                Hotel hotel = response.getBody();
//                rating.setHotel(hotel);
//                logger.debug("Successfully enriched rating {} with hotel {}", rating.getRatingId(), hotel.getId());
//            } else {
//                logger.warn("Failed to fetch hotel details for hotel ID: {} - Response status: {}", 
//                    hotelId, response.getStatusCode());
//            }
            Hotel hotel = hotelService.getHotel(hotelId);
            rating.setHotel(hotel);
            
        } catch (RestClientException e) {
            logger.error("Failed to fetch hotel details for rating {}: {}", rating.getRatingId(), e.getMessage());
        }
        
        return rating;
    }

    /**
     * Health check method to verify external service connectivity
     */
    public boolean checkExternalServicesHealth() {
        boolean ratingServiceHealthy = checkServiceHealth(ratingServiceUrl + "/actuator/health");
        boolean hotelServiceHealthy = checkServiceHealth(hotelServiceUrl + "/actuator/health");
        
        logger.info("External services health - Rating: {}, Hotel: {}", 
            ratingServiceHealthy, hotelServiceHealthy);
        
        return ratingServiceHealthy && hotelServiceHealthy;
    }

    private boolean checkServiceHealth(String healthUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.error("Health check failed for URL {}: {}", healthUrl, e.getMessage());
            return false;
        }
    }
}