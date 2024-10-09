package com.itschool.hotelResvMgt.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class PackageDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_type", nullable = false)
    private PackageDealType PackageType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "services")
    private List<String> services;

    public void setServicesByPackageType(PackageDealType PackageType) {
        switch (PackageType) {
            case BED_BREAKFAST:
                services = Arrays.asList("Accommodation", "Breakfast");
                break;
            case HALF_BOARD:
                services = Arrays.asList("Accommodation", "Breakfast", "Dinner");
            case FULL_BOARD:
                services = Arrays.asList("Accommodation", "Breakfast", "Lunch", "Dinner");
            case ALL_INCLUSIVE:
                services = Arrays.asList("Accommodation", "Breakfast", "Lunch", "Dinner",
                        "Alcoholic and non-alcoholic drinks", "Water sports", "Pool and beach access included");
            default:
                throw new IllegalArgumentException("Unknown package type");
        }
    }
}