package com.github.charlesluxinger.gisapi.infra.repository;

import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import reactor.core.publisher.Mono;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
public interface PartnerRepositoryCustom {

    Mono<PartnerDocument> findNearbyByLongitudeAndLatitude(final double longitude, final double latitude);
    Mono<PartnerDocument> insertIfNotExists(PartnerDocument partners);

}
