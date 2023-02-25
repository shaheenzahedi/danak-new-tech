package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.UnitListDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.UnitList}.
 */
public interface UnitListService {
    /**
     * Save a unitList.
     *
     * @param unitListDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UnitListDTO> save(UnitListDTO unitListDTO);

    /**
     * Updates a unitList.
     *
     * @param unitListDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UnitListDTO> update(UnitListDTO unitListDTO);

    /**
     * Partially updates a unitList.
     *
     * @param unitListDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UnitListDTO> partialUpdate(UnitListDTO unitListDTO);

    /**
     * Get all the unitLists.
     *
     * @return the list of entities.
     */
    Flux<UnitListDTO> findAll();

    /**
     * Returns the number of unitLists available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" unitList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UnitListDTO> findOne(String id);

    /**
     * Delete the "id" unitList.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
