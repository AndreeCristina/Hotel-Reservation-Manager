package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.entities.PackageDealType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PackageDealServiceImpl implements PackageDealService {

    @Override
    public List<String> getServicesByPackageType(PackageDealType packageType) {
        return switch (packageType) {
            case BED_BREAKFAST -> Arrays.asList("Accommodation", "Breakfast");
            case HALF_BOARD -> Arrays.asList("Accommodation", "Breakfast", "Dinner");
            case FULL_BOARD -> Arrays.asList("Accommodation", "Breakfast", "Lunch", "Dinner");
            case ALL_INCLUSIVE -> Arrays.asList("Accommodation", "Breakfast", "Lunch", "Dinner",
                    "Alcoholic and non-alcoholic drinks", "Water sports", "Pool and beach access included");
            default -> throw new IllegalArgumentException("Unknown package type");
        };
    }
}