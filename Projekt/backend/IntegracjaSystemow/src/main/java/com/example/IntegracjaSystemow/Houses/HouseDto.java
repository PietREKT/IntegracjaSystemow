package com.example.IntegracjaSystemow.Houses;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link House}
 */
@Value
public class HouseDto implements Serializable {
    String city;
    Double pricePerMeter;
    Double area;
    String year;
}