package org.danak.backend.repository;

import org.danak.backend.domain.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends ReactiveMongoRepository<City, String> {}
