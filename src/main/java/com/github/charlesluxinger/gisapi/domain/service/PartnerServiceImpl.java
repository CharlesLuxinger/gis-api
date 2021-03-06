package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.exceptions.InvalidObjectIdException;
import com.github.charlesluxinger.gisapi.domain.exceptions.PartnerDocumentDuplicatedException;
import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.github.charlesluxinger.gisapi.infra.config.CacheConfig.CACHE_NAME;
import static org.bson.types.ObjectId.isValid;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */

@Service
@Validated
@AllArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository repository;

    @Override
    @Cacheable(value = CACHE_NAME, key = "#id")
    public Mono<Partner> findById(@NotBlank final String id) {
        if (isValid(id)) {
            return repository
                    .findById(id)
                    .map(PartnerDocument::toDomain);
        }

        return Mono.error(new InvalidObjectIdException(id));
    }

    @Override
    public Mono<Partner> insertIfNotExists(@Valid @NotNull final Partner partners) {
        return repository
                .insert(PartnerDocument.of(partners))
                .map(PartnerDocument::toDomain)
                .onErrorMap(e -> errorMap(partners, e));
    }

    @Override
    @Cacheable(value = CACHE_NAME, keyGenerator = "findNearbyAndCoverageAreaKeyGenerator")
    public Mono<Partner> findNearbyAndCoverageArea(final double longitude, final double latitude) {
        return repository
                .findNearbyAndCoverageArea(longitude, latitude)
                .map(PartnerDocument::toDomain);
    }

    private Throwable errorMap(final Partner partners, final Throwable err) {
        return err.getClass().equals(DuplicateKeyException.class) ?
                new PartnerDocumentDuplicatedException(partners.getDocument()) :
                err;
    }

}
