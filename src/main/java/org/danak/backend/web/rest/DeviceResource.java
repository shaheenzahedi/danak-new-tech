package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.DeviceRepository;
import org.danak.backend.service.DeviceService;
import org.danak.backend.service.dto.DeviceDTO;
import org.danak.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link org.danak.backend.domain.Device}.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceService deviceService;

    private final DeviceRepository deviceRepository;

    public DeviceResource(DeviceService deviceService, DeviceRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param deviceDTO the deviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceDTO, or with status {@code 400 (Bad Request)} if the device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public Mono<ResponseEntity<DeviceDTO>> createDevice(@RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to save Device : {}", deviceDTO);
        if (deviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return deviceService
            .save(deviceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/devices/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing device.
     *
     * @param id the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceDTO,
     * or with status {@code 400 (Bad Request)} if the deviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices/{id}")
    public Mono<ResponseEntity<DeviceDTO>> updateDevice(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeviceDTO deviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Device : {}, {}", id, deviceDTO);
        if (deviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deviceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deviceService
                    .update(deviceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing device, field will ignore if it is null
     *
     * @param id the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceDTO,
     * or with status {@code 400 (Bad Request)} if the deviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DeviceDTO>> partialUpdateDevice(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeviceDTO deviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Device partially : {}, {}", id, deviceDTO);
        if (deviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deviceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeviceDTO> result = deviceService.partialUpdate(deviceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("/devices")
    public Mono<List<DeviceDTO>> getAllDevices() {
        log.debug("REST request to get all Devices");
        return deviceService.findAll().collectList();
    }

    /**
     * {@code GET  /devices} : get all the devices as a stream.
     * @return the {@link Flux} of devices.
     */
    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DeviceDTO> getAllDevicesAsStream() {
        log.debug("REST request to get all Devices as a stream");
        return deviceService.findAll();
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the deviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public Mono<ResponseEntity<DeviceDTO>> getDevice(@PathVariable String id) {
        log.debug("REST request to get Device : {}", id);
        Mono<DeviceDTO> deviceDTO = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceDTO);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the deviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public Mono<ResponseEntity<Void>> deleteDevice(@PathVariable String id) {
        log.debug("REST request to delete Device : {}", id);
        return deviceService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                        .build()
                )
            );
    }
}
