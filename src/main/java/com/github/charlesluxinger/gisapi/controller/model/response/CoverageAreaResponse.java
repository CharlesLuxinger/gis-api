package com.github.charlesluxinger.gisapi.controller.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.charlesluxinger.gisapi.controller.model.response.CoordinateType.MULTI_POLYGON;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@ToString
@AllArgsConstructor
public class CoverageAreaResponse {

    @Schema(example = "MultiPolygon")
    @NotNull
    private final CoordinateType type;

    @Schema(example = "[[[[-46.57421, -21.785741]]]]")
    @NotNull
    private final List<List<List<List<Double>>>> coordinates;

    public static CoverageAreaResponse of(final GeoJsonMultiPolygon coordinates) {
        var points = coordinates
                .getCoordinates()
                .stream()
                .map(Polygon::getPoints)
                .map(CoverageAreaResponse::getPoints)
                .map(Collections::singletonList)
                .collect(Collectors.toList());

        return new CoverageAreaResponse(MULTI_POLYGON, points);
    }

    private static List<List<Double>> getPoints(final List<Point> poly) {
        return poly
                .stream()
                .map(point -> List.of(point.getX(), point.getY()))
                .collect(Collectors.toList());
    }

}
