package org.danak.backend.repository;

import org.danak.backend.domain.UnitList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the UnitList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitListRepository extends ReactiveMongoRepository<UnitList, String> {}
