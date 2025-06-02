package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
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

    @Enumerated(EnumType.STRING)
    private Type house_type;

    @ManyToOne
    User user;


    public Double getPriceTotal(){
        if (price_per_meter == null || area == null){
            return null;
        }
        return price_per_meter * area;
    }

    protected enum Type{
        HOUSING_BLOCK,
        APARTMENT_BUILDING,
        TENEMENT
    }
}