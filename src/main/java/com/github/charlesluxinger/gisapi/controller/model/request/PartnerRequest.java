package com.github.charlesluxinger.gisapi.controller.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.charlesluxinger.gisapi.controller.config.GeoJsonPointDeserializer;
import com.github.charlesluxinger.gisapi.domain.model.Partner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@Builder
@Schema(name = "Partner Payload")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PartnerRequest {

    @Schema(example = "Adega da Cerveja - Pinheiros")
    @NotBlank
    private final String tradingName;

    @Schema(example = "Zé da Silva")
    @NotBlank
    private final String ownerName;

    @Schema(example = "1432132123891/0001")
    @NotBlank
    @EqualsAndHashCode.Include
    private final String document;

    @Schema(example = "{\"type\": \"MultiPolygon\", \"coordinates\": [[[[30, 20], [45, 40], [10, 40], [30, 20]]]]}")
    @NotNull
    private final GeoJsonMultiPolygon coverageArea;

    @Schema(example = "{\"type\": \"Point\", \"coordinates\": [-46.57421, -21.785741]}")
    @NotNull
    @JsonDeserialize(using = GeoJsonPointDeserializer.class)
    private final GeoJsonPoint address;

    public Partner toDomain() {
        return Partner
                .builder()
                .tradingName(this.tradingName)
                .ownerName(this.ownerName)
                .document(this.document)
                .coverageArea(this.coverageArea)
                .address(this.address)
                .build();
    }
}
