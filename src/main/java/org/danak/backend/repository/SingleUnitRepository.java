package org.danak.backend.repository;

import org.danak.backend.domain.SingleUnit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the SingleUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleUnitRepository extends ReactiveMongoRepository<SingleUnit, String> {}
