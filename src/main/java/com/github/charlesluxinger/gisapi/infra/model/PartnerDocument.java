package com.github.charlesluxinger.gisapi.infra.model;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.PARTNERS_COLLECTION_NAME;
import static org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID;

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
    public static final String ADDRESS = "address";
    public static final String DOCUMENT = "document";

    @NotBlank
    @MongoId(OBJECT_ID)
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
    @Field(ADDRESS)
    private GeoJsonPoint address;

    public Partner toDomain() {
        return Partner
                .builder()
                .id(this.id)
                .tradingName(this.tradingName)
                .ownerName(this.ownerName)
                .document(this.document)
                .coverageArea(this.coverageArea)
                .address(this.address)
                .build();
    }

    public static PartnerDocument of(final Partner partner) {
        return PartnerDocument
                .builder()
                .id(partner.getId())
                .tradingName(partner.getTradingName())
                .ownerName(partner.getOwnerName())
                .document(partner.getDocument())
                .coverageArea(partner.getCoverageArea())
                .address(partner.getAddress())
                .build();
    }
}
