package org.danak.backend.repository;

import org.danak.backend.domain.Province;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Province entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceRepository extends ReactiveMongoRepository<Province, String> {}
