package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.CountryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CountryDTO> save(CountryDTO countryDTO);

    /**
     * Updates a country.
     *
     * @param countryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CountryDTO> update(CountryDTO countryDTO);

    /**
     * Partially updates a country.
     *
     * @param countryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CountryDTO> partialUpdate(CountryDTO countryDTO);

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    Flux<CountryDTO> findAll();

    /**
     * Returns the number of countries available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CountryDTO> findOne(String id);

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
