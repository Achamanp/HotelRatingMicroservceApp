package com.hotelService.services;

import java.util.List;

import com.hotelService.entities.Hotel;

public interface HotelService {
	
	public Hotel createHotel(Hotel hotel);
	public List<Hotel> getAllHotel();
	public Hotel getHotel(String id);
	public Hotel updateHotel(String hid, Hotel hotel);
	public void deleteHotel(String hid);

}
