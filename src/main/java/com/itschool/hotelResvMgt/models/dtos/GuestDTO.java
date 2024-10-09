package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

@Data
public class GuestDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDTO address;
}