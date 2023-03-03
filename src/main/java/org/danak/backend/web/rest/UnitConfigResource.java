package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.UnitConfigRepository;
import org.danak.backend.service.UnitConfigService;
import org.danak.backend.service.dto.UnitConfigDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.UnitConfig}.
 */
@RestController
@RequestMapping("/api")
public class UnitConfigResource {

    private final Logger log = LoggerFactory.getLogger(UnitConfigResource.class);

    private static final String ENTITY_NAME = "unitConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitConfigService unitConfigService;

    private final UnitConfigRepository unitConfigRepository;

    public UnitConfigResource(UnitConfigService unitConfigService, UnitConfigRepository unitConfigRepository) {
        this.unitConfigService = unitConfigService;
        this.unitConfigRepository = unitConfigRepository;
    }

    /**
     * {@code POST  /unit-configs} : Create a new unitConfig.
     *
     * @param unitConfigDTO the unitConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitConfigDTO, or with status {@code 400 (Bad Request)} if the unitConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-configs")
    public Mono<ResponseEntity<UnitConfigDTO>> createUnitConfig(@RequestBody UnitConfigDTO unitConfigDTO) throws URISyntaxException {
        log.debug("REST request to save UnitConfig : {}", unitConfigDTO);
        if (unitConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return unitConfigService
            .save(unitConfigDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/unit-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /unit-configs/:id} : Updates an existing unitConfig.
     *
     * @param id the id of the unitConfigDTO to save.
     * @param unitConfigDTO the unitConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitConfigDTO,
     * or with status {@code 400 (Bad Request)} if the unitConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-configs/{id}")
    public Mono<ResponseEntity<UnitConfigDTO>> updateUnitConfig(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UnitConfigDTO unitConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UnitConfig : {}, {}", id, unitConfigDTO);
        if (unitConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return unitConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return unitConfigService
                    .update(unitConfigDTO)
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
     * {@code PATCH  /unit-configs/:id} : Partial updates given fields of an existing unitConfig, field will ignore if it is null
     *
     * @param id the id of the unitConfigDTO to save.
     * @param unitConfigDTO the unitConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitConfigDTO,
     * or with status {@code 400 (Bad Request)} if the unitConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-configs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UnitConfigDTO>> partialUpdateUnitConfig(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UnitConfigDTO unitConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitConfig partially : {}, {}", id, unitConfigDTO);
        if (unitConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return unitConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UnitConfigDTO> result = unitConfigService.partialUpdate(unitConfigDTO);

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
     * {@code GET  /unit-configs} : get all the unitConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitConfigs in body.
     */
    @GetMapping("/unit-configs")
    public Mono<List<UnitConfigDTO>> getAllUnitConfigs() {
        log.debug("REST request to get all UnitConfigs");
        return unitConfigService.findAll().collectList();
    }

    /**
     * {@code GET  /unit-configs} : get all the unitConfigs as a stream.
     * @return the {@link Flux} of unitConfigs.
     */
    @GetMapping(value = "/unit-configs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UnitConfigDTO> getAllUnitConfigsAsStream() {
        log.debug("REST request to get all UnitConfigs as a stream");
        return unitConfigService.findAll();
    }

    /**
     * {@code GET  /unit-configs/:id} : get the "id" unitConfig.
     *
     * @param id the id of the unitConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-configs/{id}")
    public Mono<ResponseEntity<UnitConfigDTO>> getUnitConfig(@PathVariable String id) {
        log.debug("REST request to get UnitConfig : {}", id);
        Mono<UnitConfigDTO> unitConfigDTO = unitConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitConfigDTO);
    }

    /**
     * {@code DELETE  /unit-configs/:id} : delete the "id" unitConfig.
     *
     * @param id the id of the unitConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-configs/{id}")
    public Mono<ResponseEntity<Void>> deleteUnitConfig(@PathVariable String id) {
        log.debug("REST request to delete UnitConfig : {}", id);
        return unitConfigService
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
