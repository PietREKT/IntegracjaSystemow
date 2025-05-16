package com.example.IntegracjaSystemow.Houses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "house")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String city;

    private Double price_per_meter;

    private Double area;

    private LocalDate date;

    private enum type{
        HOUSING_BLOCK,
        APARTMENT_BUILDING,
        TENEMENT
    };
}