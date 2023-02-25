package org.danak.backend.repository;

import org.danak.backend.domain.Centre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Centre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentreRepository extends ReactiveMongoRepository<Centre, String> {}
