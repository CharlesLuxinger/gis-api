package com.github.charlesluxinger.gisapi.controller;

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
import org.springframework.data.mongodb.core.index.Index;

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
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE;

/**
 * @author Charles Luxinger
 * @version 1.0.0 09/12/20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PartnerControllerImplTest {

    private static final String ID = "5fd003a8e6c31b4012d5f55a";
    private static final String DOCUMENT = "02.546.716/00170";
    private static final String TRADING_NAME = "Boteco Caindo aos Pedaços";
    private static final String OWNER_NAME = "Ze da Esquina";
    private static final String MULTI_POLYGON = "MultiPolygon";
    private static final String POINT = "Point";
    private static final String BODY = "{\"tradingName\":\"Boteco Caindo aos Pedaços\",\"ownerName\":\"Ze da Esquina\",\"document\":\"02.546.716/00170\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[0,1],[2,3],[4,5],[0,1]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[0,1]}}";

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
        repository.insert(buildPartnerDocument()).block();

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
    @DisplayName("should return status 400 when is not valid id")
    void should_return_status_400_when_is_not_valid_id() {
        var invalidId = "invalidId";

        given()
                .pathParam("id", invalidId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .expect()
                .statusCode(400)
            .when()
                .get("/{id}")
            .then()
                .body("status", is(400))
                .body("title", equalTo("Bad Request"))
                .body("path", equalTo(String.format("/partner/%s", invalidId)))
                .body("detail", equalTo(String.format("Invalid ObjectId #%s", invalidId)))
                .body("timestamp", is(notNullValue()));
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
            .body("path", equalTo(String.format("/partner/%s", ID)))
            .body("detail", equalTo(String.format("Partner '#%s' Not Found", ID)))
            .body("timestamp", is(notNullValue()));
    }

    @Test
    @DisplayName("should return status 200 when find by longitude and latitude")
    void should_return_status_200_when_find_by_longitude_and_latitude() {
        template.indexOps(PARTNERS_COLLECTION_NAME)
                .ensureIndex(new GeospatialIndex(ADDRESS + COORDINATES).typed(GEO_2DSPHERE))
                .block();

        repository.insert(buildPartnerDocument()).block();

        given()
            .queryParam(LONGITUDE, 0.)
            .queryParam(LATITUDE, 1.)
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
                .flatMap($ -> repository.insert(buildPartnerDocument()))
                .block();

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
                .body("path", equalTo("/partner?long=1.000000&lat=2.000000"))
                .body("detail", equalTo("Not Found nearby Partner at long:1.000000 lat:2.000000"))
                .body("timestamp", is(notNullValue()));
    }

    @Test
    @DisplayName("should return status 201 when save a new partner")
    void should_return_status_201_when_save_a_new_partner() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(BODY)
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
        template.indexOps(PARTNERS_COLLECTION_NAME)
                .ensureIndex(new Index(DOCUMENT, ASC).unique())
                .flatMap($ -> repository.insert(buildPartnerDocument()))
                .block();

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(BODY)
        .expect()
            .statusCode(400)
        .when()
            .post()
        .then()
            .body("status", is(400))
            .body("title", equalTo("Bad Request"))
            .body("path", equalTo("/partner"))
            .body("detail", equalTo(String.format("Partner with document '#%s' already exists.", DOCUMENT)))
            .body("timestamp", is(notNullValue()));
    }

    private PartnerDocument buildPartnerDocument() {
        var p1 = new Point(0., 1.);
        var p2 = new Point(2., 3.);
        var p3 = new Point(4., 5.);

        return PartnerDocument
                .builder()
                .id(ID)
                .document(DOCUMENT)
                .tradingName(TRADING_NAME)
                .ownerName(OWNER_NAME)
                .address(new GeoJsonPoint(p1))
                .coverageArea(new GeoJsonMultiPolygon(List.of(new GeoJsonPolygon(p1, p2, p3, p1))))
                .build();
    }

}