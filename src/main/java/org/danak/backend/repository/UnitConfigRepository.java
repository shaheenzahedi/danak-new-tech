package org.danak.backend.repository;

import org.danak.backend.domain.UnitConfig;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the UnitConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitConfigRepository extends ReactiveMongoRepository<UnitConfig, String> {}
