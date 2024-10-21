package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

@Data
public class AddressDTO {

    private String streetAddress;
    private String city;
    private String zipCode;
    private String country;
}