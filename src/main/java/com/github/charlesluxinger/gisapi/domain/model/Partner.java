package com.github.charlesluxinger.gisapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Builder
@Getter
@ToString
public class Partner {

    private final String id;
    private final String tradingName;
    private final String ownerName;
    private final String document;
    private final GeoJsonMultiPolygon coverageArea;
    private final GeoJsonPoint address;

}
