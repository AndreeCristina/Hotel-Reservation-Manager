package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.GuestDTO;
import com.itschool.hotelResvMgt.models.entities.Guest;

public interface GuestService {

    GuestDTO mapToGuestDTO(Guest guest);
}