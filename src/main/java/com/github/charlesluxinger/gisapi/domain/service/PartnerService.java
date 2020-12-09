package com.github.charlesluxinger.gisapi.domain.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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

    public Flux<Partner> saveAll(@Valid @NotNull final Collection<Partner> partners) {
        return Flux
                .fromIterable(partners)
                .map(PartnerDocument::of)
                .collectList()
                .flatMapMany(repository::saveAll)
                .map(PartnerDocument::toDomain);
    }

    public Mono<Partner> findNearbyByLongitudeAndLatitude(final double longitude, final double latitude) {
        return repository
                .findNearbyByLongitudeAndLatitude(longitude, latitude)
                .map(PartnerDocument::toDomain);
    }
}
