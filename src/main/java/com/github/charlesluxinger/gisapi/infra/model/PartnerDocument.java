package com.github.charlesluxinger.gisapi.infra.model;

import com.github.charlesluxinger.gisapi.domain.model.Address;
import com.github.charlesluxinger.gisapi.domain.model.CoverageArea;
import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.PolygonCoordinates;
import com.mongodb.client.model.geojson.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
@Document("partners")
public class PartnerDocument {

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
    @Field
    @Indexed(unique = true, name = "document_index")
    private String document;

    @NotNull
    @Field
    private MultiPolygon coverageArea;

    @NotNull
    @Field
    private Point address;

    public Partner toDomain() {
        List<List<List<Double>>> collect = coverageArea
                .getCoordinates()
                .parallelStream()
                .map(PolygonCoordinates::getExterior)
                .collect(Collectors.toList())
                .parallelStream()
                .map(c -> c.parallelStream().map(Position::getValues).collect(Collectors.toList()))
                .collect(Collectors.toList());

        return Partner
                .builder()
                .id(this.id)
                .tradingName(this.tradingName)
                .ownerName(this.ownerName)
                .document(this.document)
                .coverageArea(CoverageArea.of(collect))
                .address(Address.of(address.getCoordinates().getValues()))
                .build();
    }

    public static PartnerDocument of(final Partner partner) {
        var coordinates = partner
                .getCoverageArea()
                .getCoordinates()
                .parallelStream()
                .map(l -> {
                    var positions = l.get(0).parallelStream().map(Position::new).collect(Collectors.toList());

                    return new PolygonCoordinates(positions, positions);
                })
                .collect(Collectors.toList());

        return PartnerDocument
                .builder()
                .id(partner.getId())
                .tradingName(partner.getTradingName())
                .ownerName(partner.getOwnerName())
                .document(partner.getDocument())
                .coverageArea(new MultiPolygon(coordinates))
                .address(new Point(new Position(partner.getAddress().getCoordinates())))
                .build();
    }
}
