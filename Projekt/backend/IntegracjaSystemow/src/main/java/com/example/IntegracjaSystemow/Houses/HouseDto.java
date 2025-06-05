package com.example.IntegracjaSystemow.Houses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link House}
 */
@Value
public class HouseDto implements Serializable {
    @NotBlank
    String city;
    @NotNull
    Double pricePerMeter;
    @NotNull
    Double area;
    @NotBlank
    String year;
}