package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

@Data
public class StaffUserDTO {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}