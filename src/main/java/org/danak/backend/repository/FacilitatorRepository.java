package org.danak.backend.repository;

import org.danak.backend.domain.Facilitator;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Facilitator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilitatorRepository extends ReactiveMongoRepository<Facilitator, String> {}
