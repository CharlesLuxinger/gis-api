package com.github.charlesluxinger.gisapi.domain.model;

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
    private final CoverageArea coverageArea;
    private final Address address;

}
