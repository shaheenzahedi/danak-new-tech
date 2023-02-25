package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.ProgressDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Progress}.
 */
public interface ProgressService {
    /**
     * Save a progress.
     *
     * @param progressDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProgressDTO> save(ProgressDTO progressDTO);

    /**
     * Updates a progress.
     *
     * @param progressDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProgressDTO> update(ProgressDTO progressDTO);

    /**
     * Partially updates a progress.
     *
     * @param progressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProgressDTO> partialUpdate(ProgressDTO progressDTO);

    /**
     * Get all the progresses.
     *
     * @return the list of entities.
     */
    Flux<ProgressDTO> findAll();

    /**
     * Returns the number of progresses available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" progress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProgressDTO> findOne(String id);

    /**
     * Delete the "id" progress.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
