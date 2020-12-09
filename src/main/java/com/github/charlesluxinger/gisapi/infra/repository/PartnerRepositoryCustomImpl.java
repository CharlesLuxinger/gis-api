package com.github.charlesluxinger.gisapi.infra.repository;

import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Mono;

import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.ADDRESS;
import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.COVERAGE_AREA;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author Charles Luxinger
 * @version 08/12/20
 */
@AllArgsConstructor
public class PartnerRepositoryCustomImpl implements PartnerRepositoryCustom {

    public static final String COORDINATES = ".coordinates";
    private final ReactiveMongoTemplate template;

    @Override
    public Mono<PartnerDocument> findNearbyAndCoverageArea(final double longitude, final double latitude) {
        var criteria = Criteria
                .where(COVERAGE_AREA)
                .intersects(new GeoJsonPoint(longitude, latitude))
                .and(ADDRESS + COORDINATES)
                .nearSphere(new Point(longitude, latitude));

        return template.findOne(query(criteria), PartnerDocument.class);
    }

}
