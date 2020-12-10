package com.github.charlesluxinger.gisapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.github.charlesluxinger.gisapi.domain.model.CoordinateType.MULTI_POLYGON;
import static java.util.Collections.singletonList;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@ToString
@AllArgsConstructor
public class CoverageArea {

    private final CoordinateType type;
    private final List<List<List<List<Double>>>> coordinates;

    public static CoverageArea of(final List<List<List<Double>>> coordinates) {
        return new CoverageArea(MULTI_POLYGON, singletonList(coordinates));
    }

}
