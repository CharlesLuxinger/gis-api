package com.github.charlesluxinger.gisapi.controller;

import com.github.charlesluxinger.gisapi.controller.model.request.PartnerRequest;
import com.github.charlesluxinger.gisapi.controller.model.response.PartnerResponse;
import com.github.charlesluxinger.gisapi.domain.exceptions.InvalidObjectIdException;
import com.github.charlesluxinger.gisapi.domain.exceptions.PartnerDocumentDuplicatedException;
import com.github.charlesluxinger.gisapi.domain.service.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.github.charlesluxinger.gisapi.controller.PartnerControllerImpl.PARTNER_PATH;
import static com.github.charlesluxinger.gisapi.controller.model.exception.ApiExceptionResponse.buildBadRequestResponse;
import static com.github.charlesluxinger.gisapi.controller.model.exception.ApiExceptionResponse.buildNotFoundResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@RestController
@RequestMapping(value = PARTNER_PATH, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PartnerControllerImpl implements PartnerController {

    private final PartnerService service;

    protected static final String PARTNER_PATH = "/partner";
    protected static final String FIND_BY_ID_PATH_LOG = PARTNER_PATH + "/%s";
    private static final String QUERIES_PARAM = "?long=%f&lat=%f";
    private static final String PARTNER_NOT_FOUND_DETAIL = "Partner '#%s' Not Found";
    private static final String NOT_FOUND_NEARBY_PARTNER_ERROR = "Not Found nearby Partner at long:%f lat:%f";
    protected static final String LONGITUDE = "long";
    protected static final String LATITUDE = "lat";

    @Override
    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity> findById(@PathVariable final String id){
        var path = String.format(FIND_BY_ID_PATH_LOG, id);

        return service
                .findById(id)
                .map(p -> ResponseEntity.ok(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .defaultIfEmpty(buildNotFoundResponse(path, String.format(PARTNER_NOT_FOUND_DETAIL, id)))
                .onErrorResume(err -> mapError(err, path, InvalidObjectIdException.class));
    }

    @Override
    @GetMapping
    public Mono<ResponseEntity> findNearbyAndCoverageArea(@RequestParam(LONGITUDE) final double longitude,
                                                          @RequestParam(LATITUDE) final double latitude){
        return service
                .findNearbyAndCoverageArea(longitude, latitude)
                .map(p -> ResponseEntity.ok(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .defaultIfEmpty(buildNotFoundResponse(String.format(PARTNER_PATH + QUERIES_PARAM, longitude, latitude),
                                                      String.format(NOT_FOUND_NEARBY_PARTNER_ERROR, longitude, latitude)));
    }

    @Override
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> save(@RequestBody final PartnerRequest partners){
        return Mono
                .just(partners)
                .map(PartnerRequest::toDomain)
                .flatMap(service::insertIfNotExists)
                .map(p -> ResponseEntity.created(URI.create(PARTNER_PATH)).body(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .onErrorResume(err -> mapError(err, PARTNER_PATH, PartnerDocumentDuplicatedException.class));
    }

    private Mono<ResponseEntity> mapError(final Throwable err, final String path , final Class<? extends RuntimeException> clazz) {
        return err.getClass() != clazz ? Mono.error(err) : Mono.just(buildBadRequestResponse(path, err.getLocalizedMessage()));
    }

}
