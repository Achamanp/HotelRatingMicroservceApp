package com.hotelService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelService.entities.Hotel;
@Repository
public interface HotelRepository extends JpaRepository<Hotel, String>{

}
