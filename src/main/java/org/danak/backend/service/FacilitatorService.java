package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.FacilitatorDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Facilitator}.
 */
public interface FacilitatorService {
    /**
     * Save a facilitator.
     *
     * @param facilitatorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FacilitatorDTO> save(FacilitatorDTO facilitatorDTO);

    /**
     * Updates a facilitator.
     *
     * @param facilitatorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FacilitatorDTO> update(FacilitatorDTO facilitatorDTO);

    /**
     * Partially updates a facilitator.
     *
     * @param facilitatorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FacilitatorDTO> partialUpdate(FacilitatorDTO facilitatorDTO);

    /**
     * Get all the facilitators.
     *
     * @return the list of entities.
     */
    Flux<FacilitatorDTO> findAll();

    /**
     * Returns the number of facilitators available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" facilitator.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FacilitatorDTO> findOne(String id);

    /**
     * Delete the "id" facilitator.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
