package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */

@Service
@Validated
@AllArgsConstructor
public class PartnerService {

    private final PartnerRepository repository;

    public Mono<Partner> findById(@NotBlank final String id) {
        return repository
                .findById(id)
                .map(PartnerDocument::toDomain);
    }

    public Mono<Partner> insertIfNotExists(@Valid @NotNull final Partner partners) {
        return repository.existsPartnerDocumentByDocument(partners.getDocument())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalStateException(String.format("Partner with document '#%s' already exists.", partners.getDocument())));
                    }

                    return repository.insert(PartnerDocument.of(partners)).map(PartnerDocument::toDomain);
                });
    }

    public Mono<Partner> findNearbyAndCoverageArea(final double longitude, final double latitude) {
        return repository
                .findNearbyAndCoverageArea(longitude, latitude)
                .map(PartnerDocument::toDomain);
    }
}
