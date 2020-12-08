package com.github.charlesluxinger.gisapi.service;

import com.github.charlesluxinger.gisapi.domain.model.Partner;
import com.github.charlesluxinger.gisapi.infra.domain.model.PartnerDocument;
import com.github.charlesluxinger.gisapi.infra.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */

@Service
@Validated
@AllArgsConstructor
public class PartnerService {

    private final PartnerRepository repository;

    public Mono<Partner> findById(final String id) {
        return repository
                .findById(id)
                .map(PartnerDocument::toDomain);
    }
}
