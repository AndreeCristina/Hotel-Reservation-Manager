package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.AddressDTO;
import com.itschool.hotelResvMgt.models.dtos.GuestDTO;
import com.itschool.hotelResvMgt.models.entities.Address;
import com.itschool.hotelResvMgt.models.entities.Guest;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl implements GuestService {
    @Override
    public GuestDTO mapToGuestDTO(Guest guest) {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setId(guest.getId());
        guestDTO.setEmail(guest.getEmail());
        guestDTO.setLastName(guest.getLastName());
        guestDTO.setFirstName(guest.getFirstName());
        guestDTO.setPhoneNumber(guest.getPhoneNumber());
        guestDTO.setAddress(mapToAddressDTO(guest.getAddress()));

        return guestDTO;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreetAddress(address.getStreetAddress());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());

        return addressDTO;
    }
}