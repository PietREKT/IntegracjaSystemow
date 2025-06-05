package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "house")
@ToString
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String city;

    private Double pricePerMeter;

    private Double area;

    @NotNull
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