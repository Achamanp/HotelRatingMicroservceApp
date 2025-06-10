package com.hotelService.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelService.entities.Hotel;
import com.hotelService.exceptions.ResourceNotFoundException;
import com.hotelService.repositories.HotelRepository;
import com.hotelService.services.HotelService;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotel(String id) {
        return hotelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));
    }

    @Override
    public Hotel updateHotel(String hid, Hotel hotel) {
        Hotel existing = hotelRepository.findById(hid)
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hid));

        existing.setName(hotel.getName());
        existing.setLocation(hotel.getLocation());
        existing.setAbout(hotel.getAbout());

        return hotelRepository.save(existing);
    }

    @Override
    public void deleteHotel(String hid) {
        Hotel existing = hotelRepository.findById(hid)
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hid));

        hotelRepository.delete(existing);
    }
}
