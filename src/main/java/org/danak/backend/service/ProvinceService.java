package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.ProvinceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Province}.
 */
public interface ProvinceService {
    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProvinceDTO> save(ProvinceDTO provinceDTO);

    /**
     * Updates a province.
     *
     * @param provinceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProvinceDTO> update(ProvinceDTO provinceDTO);

    /**
     * Partially updates a province.
     *
     * @param provinceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProvinceDTO> partialUpdate(ProvinceDTO provinceDTO);

    /**
     * Get all the provinces.
     *
     * @return the list of entities.
     */
    Flux<ProvinceDTO> findAll();

    /**
     * Returns the number of provinces available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" province.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProvinceDTO> findOne(String id);

    /**
     * Delete the "id" province.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
