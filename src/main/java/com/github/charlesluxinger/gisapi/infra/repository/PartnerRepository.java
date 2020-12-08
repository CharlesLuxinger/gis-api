package com.github.charlesluxinger.gisapi.infra.repository;

import com.github.charlesluxinger.gisapi.infra.domain.model.PartnerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@Repository
public interface PartnerRepository extends ReactiveMongoRepository<PartnerDocument, String> {}
