package com.FirstMicroServicesApplication.controller;

import com.FirstMicroServicesApplication.entities.User;
import com.FirstMicroServicesApplication.exception.UserNotFoundException;
import com.FirstMicroServicesApplication.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserNotFoundException {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }
    
    
    int retryCount = 0;

//   Get user by ID
//    @CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
//    @Retry(name = "ratingHotelService")
    @GetMapping("/{userId}")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<User> getUserById(@PathVariable String userId) throws UserNotFoundException {
        logger.info("Processing request for user: {}", userId);
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
    	logger.info("Fallback is executed because service is down : " + ex.getMessage());
    	User user = new User();
    	user.setEmail("dummy@gmail.com");
    	user.setUsername("Dummy");
    	user.setAbout("This user is created as dummy beacuse some services is down");
    	user.setUserId("235658754");
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Update user
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userId) throws UserNotFoundException {
        User updatedUser = userService.updateUser(user, userId);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

