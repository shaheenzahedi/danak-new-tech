package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.UnitListRepository;
import org.danak.backend.service.UnitListService;
import org.danak.backend.service.dto.UnitListDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.UnitList}.
 */
@RestController
@RequestMapping("/api")
public class UnitListResource {

    private final Logger log = LoggerFactory.getLogger(UnitListResource.class);

    private static final String ENTITY_NAME = "unitList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitListService unitListService;

    private final UnitListRepository unitListRepository;

    public UnitListResource(UnitListService unitListService, UnitListRepository unitListRepository) {
        this.unitListService = unitListService;
        this.unitListRepository = unitListRepository;
    }

    /**
     * {@code POST  /unit-lists} : Create a new unitList.
     *
     * @param unitListDTO the unitListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitListDTO, or with status {@code 400 (Bad Request)} if the unitList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-lists")
    public Mono<ResponseEntity<UnitListDTO>> createUnitList(@RequestBody UnitListDTO unitListDTO) throws URISyntaxException {
        log.debug("REST request to save UnitList : {}", unitListDTO);
        if (unitListDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return unitListService
            .save(unitListDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/unit-lists/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /unit-lists/:id} : Updates an existing unitList.
     *
     * @param id the id of the unitListDTO to save.
     * @param unitListDTO the unitListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitListDTO,
     * or with status {@code 400 (Bad Request)} if the unitListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-lists/{id}")
    public Mono<ResponseEntity<UnitListDTO>> updateUnitList(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UnitListDTO unitListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UnitList : {}, {}", id, unitListDTO);
        if (unitListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return unitListRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return unitListService
                    .update(unitListDTO)
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
     * {@code PATCH  /unit-lists/:id} : Partial updates given fields of an existing unitList, field will ignore if it is null
     *
     * @param id the id of the unitListDTO to save.
     * @param unitListDTO the unitListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitListDTO,
     * or with status {@code 400 (Bad Request)} if the unitListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UnitListDTO>> partialUpdateUnitList(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody UnitListDTO unitListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitList partially : {}, {}", id, unitListDTO);
        if (unitListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return unitListRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UnitListDTO> result = unitListService.partialUpdate(unitListDTO);

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
     * {@code GET  /unit-lists} : get all the unitLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitLists in body.
     */
    @GetMapping("/unit-lists")
    public Mono<List<UnitListDTO>> getAllUnitLists() {
        log.debug("REST request to get all UnitLists");
        return unitListService.findAll().collectList();
    }

    /**
     * {@code GET  /unit-lists} : get all the unitLists as a stream.
     * @return the {@link Flux} of unitLists.
     */
    @GetMapping(value = "/unit-lists", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UnitListDTO> getAllUnitListsAsStream() {
        log.debug("REST request to get all UnitLists as a stream");
        return unitListService.findAll();
    }

    /**
     * {@code GET  /unit-lists/:id} : get the "id" unitList.
     *
     * @param id the id of the unitListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-lists/{id}")
    public Mono<ResponseEntity<UnitListDTO>> getUnitList(@PathVariable String id) {
        log.debug("REST request to get UnitList : {}", id);
        Mono<UnitListDTO> unitListDTO = unitListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitListDTO);
    }

    /**
     * {@code DELETE  /unit-lists/:id} : delete the "id" unitList.
     *
     * @param id the id of the unitListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-lists/{id}")
    public Mono<ResponseEntity<Void>> deleteUnitList(@PathVariable String id) {
        log.debug("REST request to delete UnitList : {}", id);
        return unitListService
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
