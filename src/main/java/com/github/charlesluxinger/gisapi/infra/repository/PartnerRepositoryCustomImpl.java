package com.github.charlesluxinger.gisapi.infra.repository;

import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

/**
 * @author Charles Luxinger
 * @version 08/12/20
 */
@AllArgsConstructor
public class PartnerRepositoryCustomImpl implements PartnerRepositoryCustom {

    private static final String ADDRESS_COORDINATES = "address.coordinates";
    private final ReactiveMongoTemplate template;

    @Override
    public Mono<PartnerDocument> findNearbyByLongitudeAndLatitude(final double longitude, final double latitude) {
        var query =  Query.query(Criteria.where("coverageArea").intersects(new GeoJsonPoint(longitude, latitude)));

        return template.findOne(query, PartnerDocument.class);
    }
}
