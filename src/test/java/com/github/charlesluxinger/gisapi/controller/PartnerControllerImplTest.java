package com.github.charlesluxinger.gisapi.controller;

import com.github.charlesluxinger.gisapi.controller.model.PartnerPayload;
import com.github.charlesluxinger.gisapi.domain.model.Address;
import com.github.charlesluxinger.gisapi.domain.model.CoverageArea;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

import java.util.List;

import static com.github.charlesluxinger.gisapi.controller.PartnerControllerImpl.LATITUDE;
import static com.github.charlesluxinger.gisapi.controller.PartnerControllerImpl.LONGITUDE;
import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.ADDRESS;
import static com.github.charlesluxinger.gisapi.infra.model.PartnerDocument.PARTNERS_COLLECTION_NAME;
import static com.github.charlesluxinger.gisapi.infra.repository.PartnerRepositoryCustomImpl.COORDINATES;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE;

/**
 * @author Charles Luxinger
 * @version 1.0.0 09/12/20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PartnerControllerImplTest {

    private static final String ID = "5fd003a8e6c31b4012d5f55a";
    public static final String DOCUMENT = "02.546.716/00170";
    public static final String TRADING_NAME = "Adega Osasco";
    public static final String OWNER_NAME = "Ze da Ambev";
    public static final String MULTI_POLYGON = "MultiPolygon";
    public static final String POINT = "Point";

    @LocalServerPort
    private int port;

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    PartnerRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = "api/v1/partner";
        RestAssured.port = port;

        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should return status 200 when find by id")
    void should_return_status_200_when_find_by_id() {
        repository.insert(buildPartnerDocument(ID)).block();

        given()
            .pathParam("id", ID)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .expect()
            .statusCode(200)
        .when()
            .get("/{id}")
        .then()
            .body("id", equalTo(ID))
            .body("tradingName", equalTo(TRADING_NAME))
            .body("ownerName", equalTo(OWNER_NAME))
            .body("document", equalTo(DOCUMENT))
            .body("coverageArea.type", equalTo(MULTI_POLYGON))
            .body("coverageArea.coordinates", not(emptyArray()))
            .body("address.type", equalTo(POINT))
            .body("address.coordinates", not(emptyArray()));
    }

    @Test
    @DisplayName("should return status 404 when find by id")
    void should_return_status_404_when_find_by_id() {
        given()
            .pathParam("id", ID)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .expect()
            .statusCode(404)
        .when()
            .get("/{id}")
        .then()
            .body("status", is(404))
            .body("title", equalTo("Not Found"))
            .body("path", equalTo(String.format("api/v1/partner/%s", ID)))
            .body("detail", equalTo(String.format("Partner '#%s' Not Found", ID)))
            .body("timestamp", is(notNullValue()));
    }

    @Test
    @DisplayName("should return status 200 when finde by longitude and latitude")
    void should_return_status_200_when_find_by_longitude_and_latitude() {
        template.indexOps(PARTNERS_COLLECTION_NAME)
                .ensureIndex(new GeospatialIndex(ADDRESS + COORDINATES).typed(GEO_2DSPHERE))
                .block();

        repository.insert(buildPartnerDocument(ID)).block();

        given()
            .queryParam(LONGITUDE, 2.)
            .queryParam(LATITUDE, 3.)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .expect()
            .statusCode(200)
        .when()
            .get()
        .then()
            .body("id", equalTo(ID))
            .body("tradingName", equalTo(TRADING_NAME))
            .body("ownerName", equalTo(OWNER_NAME))
            .body("document", equalTo(DOCUMENT))
            .body("coverageArea.type", equalTo(MULTI_POLYGON))
            .body("coverageArea.coordinates", not(emptyArray()))
            .body("address.type", equalTo(POINT))
            .body("address.coordinates", not(emptyArray()));
    }

    @Test
    @DisplayName("should return status 404 when coverage area not longitude and latitude")
    void should_return_status_404_when_coverage_area_not_support_longitude_and_latitude_() {
        template.indexOps(PARTNERS_COLLECTION_NAME)
                .ensureIndex(new GeospatialIndex(ADDRESS + COORDINATES).typed(GEO_2DSPHERE))
                .block();

        repository.insert(buildPartnerDocument(ID)).block();

        given()
                .queryParam(LONGITUDE, 1)
                .queryParam(LATITUDE, 2)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .expect()
                .statusCode(404)
            .when()
                .get()
            .then()
                .body("status", is(404))
                .body("title", equalTo("Not Found"))
                .body("path", equalTo("api/v1/partner?long=1.000000&lat=2.000000"))
                .body("detail", equalTo("Not Found nearby Partner at long:1.000000 lat:2.000000"))
                .body("timestamp", is(notNullValue()));
    }

    @Test
    @DisplayName("should return status 201 when save a new partner")
    void should_return_status_201_when_save_a_new_partner() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(buildPartnerPayload())
        .expect()
            .statusCode(201)
        .when()
            .post()
        .then()
            .body("id", notNullValue())
            .body("tradingName", equalTo(TRADING_NAME))
            .body("ownerName", equalTo(OWNER_NAME))
            .body("document", equalTo(DOCUMENT))
            .body("coverageArea.type", equalTo(MULTI_POLYGON))
            .body("coverageArea.coordinates", not(emptyArray()))
            .body("address.type", equalTo(POINT))
            .body("address.coordinates", not(emptyArray()));
    }

    @Test
    @DisplayName("should return status 400 when save an exists partner")
    void should_return_status_400_when_save_an_exists_partner() {
        repository.insert(buildPartnerDocument(ID)).block();

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(buildPartnerPayload())
        .expect()
            .statusCode(400)
        .when()
            .post()
        .then()
            .body("status", is(400))
            .body("title", equalTo("Bad Request"))
            .body("path", equalTo("api/v1/partner"))
            .body("detail", equalTo(String.format("Partner with document '#%s' already exists.", DOCUMENT)))
            .body("timestamp", is(notNullValue()));
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

    private PartnerPayload buildPartnerPayload() {
        return PartnerPayload
                .builder()
                .document(DOCUMENT)
                .tradingName(TRADING_NAME)
                .ownerName(OWNER_NAME)
                .address(Address.of(List.of(0., 1.)))
                .coverageArea(CoverageArea.of(List.of(List.of(List.of(0., 1.), List.of(2., 3.), List.of(4., 5.), List.of(0., 1.)))))
                .build();
    }

}