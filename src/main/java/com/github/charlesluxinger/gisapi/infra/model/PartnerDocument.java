package com.github.charlesluxinger.gisapi.infra.model;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
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
