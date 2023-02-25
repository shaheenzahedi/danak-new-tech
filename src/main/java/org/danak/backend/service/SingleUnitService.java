package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.SingleUnitDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.SingleUnit}.
 */
public interface SingleUnitService {
    /**
     * Save a singleUnit.
     *
     * @param singleUnitDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SingleUnitDTO> save(SingleUnitDTO singleUnitDTO);

    /**
     * Updates a singleUnit.
     *
     * @param singleUnitDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SingleUnitDTO> update(SingleUnitDTO singleUnitDTO);

    /**
     * Partially updates a singleUnit.
     *
     * @param singleUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SingleUnitDTO> partialUpdate(SingleUnitDTO singleUnitDTO);

    /**
     * Get all the singleUnits.
     *
     * @return the list of entities.
     */
    Flux<SingleUnitDTO> findAll();

    /**
     * Returns the number of singleUnits available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" singleUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SingleUnitDTO> findOne(String id);

    /**
     * Delete the "id" singleUnit.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
