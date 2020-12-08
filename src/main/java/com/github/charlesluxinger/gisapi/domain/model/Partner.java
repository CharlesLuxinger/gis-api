package com.github.charlesluxinger.gisapi.domain.model;

import com.github.charlesluxinger.gisapi.infra.model.MultiPolygon;
import com.github.charlesluxinger.gisapi.infra.model.Point;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Builder
@Getter
public class Partner {

    private final String id;
    private final String tradingName;
    private final String ownerName;
    private final String document;
    private final MultiPolygon coverageArea;
    private final Point address;

}
