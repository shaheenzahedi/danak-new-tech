package org.danak.backend.repository;

import org.danak.backend.domain.Child;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends ReactiveMongoRepository<Child, String> {}
