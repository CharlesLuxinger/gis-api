package com.github.charlesluxinger.gisapi.infra.model;

import com.github.charlesluxinger.gisapi.domain.model.Address;
import com.github.charlesluxinger.gisapi.domain.model.CoverageArea;
import com.github.charlesluxinger.gisapi.domain.model.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.PARTNERS_COLLECTION_NAME;

/**
 * Class comments go here...
 *
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Document(PARTNERS_COLLECTION_NAME)
public class PartnerDocument {

    public static final String PARTNERS_COLLECTION_NAME = "partners";
    public static final String COVERAGE_AREA = "coverageArea";
    public static final String DOCUMENT = "document";

    @NotBlank
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @NotBlank
    @Field
    private String tradingName;

    @NotBlank
    @Field
    private String ownerName;

    @NotBlank
    @Field(DOCUMENT)
    private String document;

    @NotNull
    @Field(COVERAGE_AREA)
    private GeoJsonMultiPolygon coverageArea;

    @NotNull
    @Field
    private GeoJsonPoint address;

    public Partner toDomain() {
        var coordinates = coverageArea
                .getCoordinates()
                .parallelStream()
                .map(Polygon::getPoints)
                .map(c ->
                        c.parallelStream()
                        .map(a -> List.of(a.getX(), a.getY()))
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        return Partner
                .builder()
                .id(this.id)
                .tradingName(this.tradingName)
                .ownerName(this.ownerName)
                .document(this.document)
                .coverageArea(CoverageArea.of(coordinates))
                .address(Address.of(List.of(address.getX(), address.getY())))
                .build();
    }

    public static PartnerDocument of(final Partner partner) {
        var longitude = partner.getAddress().getCoordinates().get(0);
        var latitude = partner.getAddress().getCoordinates().get(1);

        var points = partner
                .getCoverageArea()
                .getCoordinates()
                .get(0)
                .get(0)
                .parallelStream()
                .map(c -> new Point(c.get(0), c.get(1)))
                .collect(Collectors.toList());

        return builder()
                .id(partner.getId())
                .tradingName(partner.getTradingName())
                .ownerName(partner.getOwnerName())
                .document(partner.getDocument())
                .coverageArea(new GeoJsonMultiPolygon(List.of(new GeoJsonPolygon(points))))
                .address(new GeoJsonPoint(longitude, latitude))
                .build();
    }
}
