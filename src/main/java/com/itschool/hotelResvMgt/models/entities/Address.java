package com.itschool.hotelResvMgt.models.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String streetAddress;
    private String city;
    private String country;
    private String zipCode;
}
