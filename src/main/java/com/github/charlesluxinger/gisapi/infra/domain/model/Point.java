package com.github.charlesluxinger.gisapi.infra.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Point {

    private CoordinateType type;
    private List<Double> coordinates;
    
}
