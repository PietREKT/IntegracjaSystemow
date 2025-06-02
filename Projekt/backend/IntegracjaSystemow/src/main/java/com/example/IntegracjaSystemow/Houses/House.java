package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

    private Double pricePerMeter;

    private Double area;

    private Integer transactionYear;

    @ManyToOne
    User user;

    @Setter(AccessLevel.NONE)
    private Double priceTotal;

    @PrePersist
    public void setPriceTotal(){
        if (pricePerMeter == null || area == null){
            priceTotal = null;
            return;
        }
        priceTotal = pricePerMeter * area;
    }
}