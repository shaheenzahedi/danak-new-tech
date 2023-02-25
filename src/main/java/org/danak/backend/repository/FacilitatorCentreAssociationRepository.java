package org.danak.backend.repository;

import org.danak.backend.domain.FacilitatorCentreAssociation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the FacilitatorCentreAssociation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilitatorCentreAssociationRepository extends ReactiveMongoRepository<FacilitatorCentreAssociation, String> {}
