package com.itschool.hotelResvMgt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.models.dtos.AddressDTO;
import com.itschool.hotelResvMgt.models.dtos.GuestDTO;
import com.itschool.hotelResvMgt.models.entities.Address;
import com.itschool.hotelResvMgt.models.entities.Guest;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl implements GuestService {

    private final ObjectMapper objectMapper;

    public GuestServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public GuestDTO mapToGuestDTO(Guest guest) {
        GuestDTO guestDTO = objectMapper.convertValue(guest, GuestDTO.class);

        return guestDTO;
    }

    @Override
    public AddressDTO mapToAddressDTO(Address address) {
        AddressDTO addressDTO = objectMapper.convertValue(address, AddressDTO.class);

        return addressDTO;
    }
}