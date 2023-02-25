package org.danak.backend.repository;

import org.danak.backend.domain.Progress;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Progress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgressRepository extends ReactiveMongoRepository<Progress, String> {}
