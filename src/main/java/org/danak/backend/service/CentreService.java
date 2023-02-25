package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.CentreDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Centre}.
 */
public interface CentreService {
    /**
     * Save a centre.
     *
     * @param centreDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CentreDTO> save(CentreDTO centreDTO);

    /**
     * Updates a centre.
     *
     * @param centreDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CentreDTO> update(CentreDTO centreDTO);

    /**
     * Partially updates a centre.
     *
     * @param centreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CentreDTO> partialUpdate(CentreDTO centreDTO);

    /**
     * Get all the centres.
     *
     * @return the list of entities.
     */
    Flux<CentreDTO> findAll();

    /**
     * Returns the number of centres available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" centre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CentreDTO> findOne(String id);

    /**
     * Delete the "id" centre.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
