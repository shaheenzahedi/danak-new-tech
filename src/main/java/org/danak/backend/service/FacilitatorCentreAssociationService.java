package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.FacilitatorCentreAssociationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.FacilitatorCentreAssociation}.
 */
public interface FacilitatorCentreAssociationService {
    /**
     * Save a facilitatorCentreAssociation.
     *
     * @param facilitatorCentreAssociationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FacilitatorCentreAssociationDTO> save(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO);

    /**
     * Updates a facilitatorCentreAssociation.
     *
     * @param facilitatorCentreAssociationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FacilitatorCentreAssociationDTO> update(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO);

    /**
     * Partially updates a facilitatorCentreAssociation.
     *
     * @param facilitatorCentreAssociationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FacilitatorCentreAssociationDTO> partialUpdate(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO);

    /**
     * Get all the facilitatorCentreAssociations.
     *
     * @return the list of entities.
     */
    Flux<FacilitatorCentreAssociationDTO> findAll();

    /**
     * Returns the number of facilitatorCentreAssociations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" facilitatorCentreAssociation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FacilitatorCentreAssociationDTO> findOne(String id);

    /**
     * Delete the "id" facilitatorCentreAssociation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
