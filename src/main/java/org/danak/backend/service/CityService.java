package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.CityDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.City}.
 */
public interface CityService {
    /**
     * Save a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CityDTO> save(CityDTO cityDTO);

    /**
     * Updates a city.
     *
     * @param cityDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CityDTO> update(CityDTO cityDTO);

    /**
     * Partially updates a city.
     *
     * @param cityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CityDTO> partialUpdate(CityDTO cityDTO);

    /**
     * Get all the cities.
     *
     * @return the list of entities.
     */
    Flux<CityDTO> findAll();

    /**
     * Returns the number of cities available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" city.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CityDTO> findOne(String id);

    /**
     * Delete the "id" city.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
