package org.danak.backend.service;

import java.util.List;
import org.danak.backend.service.dto.DeviceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link org.danak.backend.domain.Device}.
 */
public interface DeviceService {
    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DeviceDTO> save(DeviceDTO deviceDTO);

    /**
     * Updates a device.
     *
     * @param deviceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DeviceDTO> update(DeviceDTO deviceDTO);

    /**
     * Partially updates a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DeviceDTO> partialUpdate(DeviceDTO deviceDTO);

    /**
     * Get all the devices.
     *
     * @return the list of entities.
     */
    Flux<DeviceDTO> findAll();

    /**
     * Returns the number of devices available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DeviceDTO> findOne(String id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
