package com.github.charlesluxinger.gisapi.infra.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Arrays.stream;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@AllArgsConstructor
public enum CoordinateType {

    MULTI_POLYGON("MultiPolygon"),
    POINT("Point");

    @JsonValue
    private final String value;

    public static CoordinateType fromValue(final String value) {
        return stream(values())
                .filter(c -> c.value.equals(value))
                .findAny()
                .orElse(null);
    }

}
