package com.github.charlesluxinger.gisapi.infra.repository;

import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.COVERAGE_AREA;
import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.DOCUMENT;

/**
 * @author Charles Luxinger
 * @version 08/12/20
 */
@AllArgsConstructor
public class PartnerRepositoryCustomImpl implements PartnerRepositoryCustom {

    private final ReactiveMongoTemplate template;

    @Override
    public Mono<PartnerDocument> findNearbyByLongitudeAndLatitude(final double longitude, final double latitude) {
        var query =  Query.query(Criteria.where(COVERAGE_AREA).intersects(new GeoJsonPoint(longitude, latitude)));

        return template.findOne(query, PartnerDocument.class);
    }

    @Override
    public Mono<PartnerDocument> insertIfNotExists(final PartnerDocument partners) {
        var query =  Query.query(Criteria.where(DOCUMENT).is(partners.getDocument()));

        return template
                .findOne(query, PartnerDocument.class)
                .switchIfEmpty(template.insert(partners))
                .flatMap($ ->  Mono.error(new IllegalStateException(String.format("Partner with document '#%s' already exists.", partners.getDocument()))));
    }

}
