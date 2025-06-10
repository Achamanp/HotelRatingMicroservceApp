package com.FirstMicroServicesApplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.FirstMicroServicesApplication.entities.Rating;
import com.FirstMicroServicesApplication.external.RatingService;

import lombok.Builder;

@SpringBootTest
class FirstMicroServicesApplicationTests {
	
	private RatingService ratingService;
	
	

	@Test
	void contextLoads() {
	}
	


}
