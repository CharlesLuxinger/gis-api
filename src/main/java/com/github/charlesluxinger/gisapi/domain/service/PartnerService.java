package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.bson.types.ObjectId.isValid;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */

@Service
@Validated
@AllArgsConstructor
public class PartnerService {

    protected static final String INVALID_OBJECT_ID_DETAIL = "Invalid ObjectId #%s";
    protected static final String EXISTS_ERROR_MESSAGE = "Partner with document '#%s' already exists.";
    private final PartnerRepository repository;

    public Mono<Partner> findById(@NotBlank final String id) {
        if (isValid(id)) {
            return repository
                    .findById(id)
                    .map(PartnerDocument::toDomain);
        }

        return Mono.error(new IllegalArgumentException(String.format(INVALID_OBJECT_ID_DETAIL, id)));
    }

    public Mono<Partner> insertIfNotExists(@Valid @NotNull final Partner partners) {
        return repository
                .insert(PartnerDocument.of(partners))
                .map(PartnerDocument::toDomain)
                .onErrorMap(e -> errorMap(partners, e));

    }

    public Mono<Partner> findNearbyAndCoverageArea(final double longitude, final double latitude) {
        return repository
                .findNearbyAndCoverageArea(longitude, latitude)
                .map(PartnerDocument::toDomain);
    }

    private Throwable errorMap(Partner partners, Throwable err) {
        return err.getClass().equals(DuplicateKeyException.class) ?
                new IllegalStateException(String.format(EXISTS_ERROR_MESSAGE, partners.getDocument())) :
                err;
    }

}
