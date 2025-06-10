package com.FirstMicroServicesApplication.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.FirstMicroServicesApplication.entities.Hotel;

@FeignClient("HOTELSERVICE")
public interface HotelService {
	
	
	@GetMapping("/api/hotels/{id}")
	Hotel getHotel(@PathVariable String id);

}
