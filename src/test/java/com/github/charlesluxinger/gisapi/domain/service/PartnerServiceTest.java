package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.github.charlesluxinger.gisapi.domain.model.CoordinateType.MULTI_POLYGON;
import static com.github.charlesluxinger.gisapi.domain.model.CoordinateType.POINT;
import static com.github.charlesluxinger.gisapi.domain.service.PartnerService.EXISTS_ERROR_MESSAGE;
import static com.github.charlesluxinger.gisapi.domain.service.PartnerService.INVALID_OBJECT_ID_DETAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Class comments go here...
 *
 * @author Charles Luxinger
 * @version 09/12/20
 */
@Import(PartnerService.class)
@ExtendWith(SpringExtension.class)
class PartnerServiceTest {

    private static final String ID = "5fd003a8e6c31b4012d5f55a";
    private static final String DOCUMENT = "02.546.716/00170";
    private static final String TRADING_NAME = "Adega Osasco";
    private static final String OWNER_NAME = "Ze da Ambev";

    @Autowired
    private PartnerService service;

    @MockBean
    private PartnerRepository repository;

    @Test
    void should_return_a_partner_when_find_by_id() {
        var document = buildPartnerDocument(ID);
        when(repository.findById(ID)).thenReturn(Mono.just(document));

        var partner = service.findById(ID).block();

        assertions(partner);
    }

    @Test
    void should_insert_when_not_exists() {
        var document = buildPartnerDocument(ID);

        when(repository.insert((PartnerDocument) Mockito.any())).thenReturn(Mono.just(document));

        var partner = service.insertIfNotExists(document.toDomain()).block();

        assertions(partner);
    }

    @Test
    void should_throw_exceptions_when_insert_an_exists_document() {
        var document = buildPartnerDocument(ID);

        when(repository.insert((PartnerDocument) Mockito.any())).thenReturn(Mono.error(new DuplicateKeyException("")));

        assertThrows(IllegalStateException.class,
                () -> service.insertIfNotExists(document.toDomain()).block(),
                String.format(EXISTS_ERROR_MESSAGE, DOCUMENT));
    }

    @Test
    void should_throw_exceptions_when_is_an_invalid_id() {
        var id = "invalidId";

        assertThrows(IllegalArgumentException.class,
                () -> service.findById(id).block(),
                String.format(INVALID_OBJECT_ID_DETAIL, id));
    }

    @Test
    void should_return_a_nearby_partner_when_find_by_a_valid_longitude_and_latitude() {
        var longitude = 0;
        var latitude = 1;
        var document = buildPartnerDocument(ID);

        when(repository.findNearbyAndCoverageArea(longitude, latitude)).thenReturn(Mono.just(document));

        var partner = service.findNearbyAndCoverageArea(longitude, latitude).block();

        assertions(partner);
    }

    @Test
    void should_not_return_a_nearby_partner_when_find_by_an_invalida_longitude_and_latitude() {
        var longitude = 0.;
        var latitude = 1.;
        var document = buildPartnerDocument(ID);

        when(repository.findNearbyAndCoverageArea(longitude, latitude)).thenReturn(Mono.empty());

        var partner = service.findNearbyAndCoverageArea(longitude, latitude).block();

        assertNull(partner);
    }

    private PartnerDocument buildPartnerDocument(final String id) {
        var points = List.of(new Point(0, 1), new Point(2, 3), new Point(4, 5), new Point(0, 1));

        return PartnerDocument
                .builder()
                .id(id)
                .document(DOCUMENT)
                .tradingName(TRADING_NAME)
                .ownerName(OWNER_NAME)
                .address(new GeoJsonPoint(1, 2))
                .coverageArea(new GeoJsonMultiPolygon(List.of(new GeoJsonPolygon(points))))
                .build();
    }

    private void assertions(Partner partner) {
        assertEquals(partner.getId(), ID);
        assertEquals(partner.getDocument(), DOCUMENT);
        assertEquals(partner.getOwnerName(), OWNER_NAME);
        assertEquals(partner.getTradingName(), TRADING_NAME);
        assertEquals(partner.getAddress().getType(), POINT);
        assertFalse(partner.getAddress().getCoordinates().isEmpty());
        assertEquals(partner.getCoverageArea().getType(), MULTI_POLYGON);
        assertFalse(partner.getCoverageArea().getCoordinates().isEmpty());
    }
}