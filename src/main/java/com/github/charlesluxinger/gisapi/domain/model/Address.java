package com.github.charlesluxinger.gisapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.github.charlesluxinger.gisapi.domain.model.CoordinateType.POINT;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@AllArgsConstructor
public class Address {

    private final CoordinateType type;
    private final List<Double> coordinates;

    public static Address of(final List<Double> coordinates) {
        return new Address(POINT, coordinates);
    }

}
