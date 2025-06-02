package com.example.IntegracjaSystemow.Houses;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link House}
 */
@Value
public class HouseDto implements Serializable {
    String city;
    Double price_per_meter;
    Double area;
    House.Type house_type;
    String quarter;
    String year;
}