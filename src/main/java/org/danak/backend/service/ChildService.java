package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.ChildDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Child}.
 */
public interface ChildService {
    /**
     * Save a child.
     *
     * @param childDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ChildDTO> save(ChildDTO childDTO);

    /**
     * Updates a child.
     *
     * @param childDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ChildDTO> update(ChildDTO childDTO);

    /**
     * Partially updates a child.
     *
     * @param childDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ChildDTO> partialUpdate(ChildDTO childDTO);

    /**
     * Get all the children.
     *
     * @return the list of entities.
     */
    Flux<ChildDTO> findAll();

    /**
     * Returns the number of children available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" child.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ChildDTO> findOne(String id);

    /**
     * Delete the "id" child.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
