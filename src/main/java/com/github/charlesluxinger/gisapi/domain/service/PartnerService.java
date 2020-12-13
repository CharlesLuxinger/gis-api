package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charles Luxinger
 * @version 13/12/20
 */
public interface PartnerService {

    Mono<Partner> findById(@NotBlank final String id);

    Mono<Partner> insertIfNotExists(@Valid @NotNull final Partner partners);

    Mono<Partner> findNearbyAndCoverageArea(double longitude, double latitude);

}
