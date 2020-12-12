package com.github.charlesluxinger.gisapi.controller.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.charlesluxinger.gisapi.domain.model.Partner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class comments go here...
 *
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Getter
@Builder
@Schema(name = "Partner Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerResponse {

    @Schema(example = "999")
    @NotBlank
    private final String id;

    @Schema(example = "Adega da Cerveja - Pinheiros")
    @NotBlank
    private final String tradingName;

    @Schema(example = "Zé da Silva")
    @NotBlank
    private final String ownerName;

    @Schema(example = "1432132123891/0001")
    @NotBlank
    private final String document;

    @Schema(example = "{\"type\": \"MultiPolygon\", \"coordinates\": [[[[30, 20], [45, 40], [10, 40], [30, 20]]]]}")
    @NotNull
    private final CoverageAreaResponse coverageArea;

    @NotNull
    private final AddressResponse address;

    public static PartnerResponse of(final Partner partner) {
        return PartnerResponse
                .builder()
                .id(partner.getId())
                .tradingName(partner.getTradingName())
                .ownerName(partner.getOwnerName())
                .document(partner.getDocument())
                .coverageArea(CoverageAreaResponse.of(partner.getCoverageArea()))
                .address(AddressResponse.of(partner.getAddress()))
                .build();
    }

}
