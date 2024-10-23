package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.entities.PackageDealType;

import java.util.List;

public interface PackageDealService {

    List<String> getServicesByPackageType(PackageDealType packageType);
}