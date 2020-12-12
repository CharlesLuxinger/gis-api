package com.github.charlesluxinger.gisapi.controller.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.geo.Point;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.github.charlesluxinger.gisapi.controller.model.response.CoordinateType.POINT;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@ToString
@AllArgsConstructor
public class AddressResponse {

    @Schema(example = "Point")
    @NotNull
    private final CoordinateType type;

    @Schema(example = "[-46.57421, -21.785741]")
    private final List<Double> coordinates;

    public static AddressResponse of(final Point point) {
        return new AddressResponse(POINT, List.of(point.getX(), point.getY()));
    }

}
