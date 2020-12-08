package com.github.charlesluxinger.gisapi.infra.domain.model;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Field
    private String id;

    @NotBlank
    @Field
    private String tradingName;

    @NotBlank
    @Field
    private String ownerName;

    @NotBlank
    @Field
    private String document;

    @NotNull
    @Field
    private MultiPolygon coverageArea;

    @NotNull
    @Field
    private Point address;

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
}
