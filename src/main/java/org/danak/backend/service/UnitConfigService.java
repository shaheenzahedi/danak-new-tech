package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.UnitConfigDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.UnitConfig}.
 */
public interface UnitConfigService {
    /**
     * Save a unitConfig.
     *
     * @param unitConfigDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UnitConfigDTO> save(UnitConfigDTO unitConfigDTO);

    /**
     * Updates a unitConfig.
     *
     * @param unitConfigDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UnitConfigDTO> update(UnitConfigDTO unitConfigDTO);

    /**
     * Partially updates a unitConfig.
     *
     * @param unitConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UnitConfigDTO> partialUpdate(UnitConfigDTO unitConfigDTO);

    /**
     * Get all the unitConfigs.
     *
     * @return the list of entities.
     */
    Flux<UnitConfigDTO> findAll();

    /**
     * Returns the number of unitConfigs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" unitConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UnitConfigDTO> findOne(String id);

    /**
     * Delete the "id" unitConfig.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
