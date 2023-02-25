package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.SingleUnitRepository;
import org.danak.backend.service.SingleUnitService;
import org.danak.backend.service.dto.SingleUnitDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.SingleUnit}.
 */
@RestController
@RequestMapping("/api")
public class SingleUnitResource {

    private final Logger log = LoggerFactory.getLogger(SingleUnitResource.class);

    private static final String ENTITY_NAME = "singleUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleUnitService singleUnitService;

    private final SingleUnitRepository singleUnitRepository;

    public SingleUnitResource(SingleUnitService singleUnitService, SingleUnitRepository singleUnitRepository) {
        this.singleUnitService = singleUnitService;
        this.singleUnitRepository = singleUnitRepository;
    }

    /**
     * {@code POST  /single-units} : Create a new singleUnit.
     *
     * @param singleUnitDTO the singleUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleUnitDTO, or with status {@code 400 (Bad Request)} if the singleUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/single-units")
    public Mono<ResponseEntity<SingleUnitDTO>> createSingleUnit(@RequestBody SingleUnitDTO singleUnitDTO) throws URISyntaxException {
        log.debug("REST request to save SingleUnit : {}", singleUnitDTO);
        if (singleUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singleUnitService
            .save(singleUnitDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-units/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-units/:id} : Updates an existing singleUnit.
     *
     * @param id the id of the singleUnitDTO to save.
     * @param singleUnitDTO the singleUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleUnitDTO,
     * or with status {@code 400 (Bad Request)} if the singleUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/single-units/{id}")
    public Mono<ResponseEntity<SingleUnitDTO>> updateSingleUnit(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SingleUnitDTO singleUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleUnit : {}, {}", id, singleUnitDTO);
        if (singleUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleUnitRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singleUnitService
                    .update(singleUnitDTO)
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
     * {@code PATCH  /single-units/:id} : Partial updates given fields of an existing singleUnit, field will ignore if it is null
     *
     * @param id the id of the singleUnitDTO to save.
     * @param singleUnitDTO the singleUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleUnitDTO,
     * or with status {@code 400 (Bad Request)} if the singleUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/single-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SingleUnitDTO>> partialUpdateSingleUnit(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SingleUnitDTO singleUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleUnit partially : {}, {}", id, singleUnitDTO);
        if (singleUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleUnitRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SingleUnitDTO> result = singleUnitService.partialUpdate(singleUnitDTO);

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
     * {@code GET  /single-units} : get all the singleUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleUnits in body.
     */
    @GetMapping("/single-units")
    public Mono<List<SingleUnitDTO>> getAllSingleUnits() {
        log.debug("REST request to get all SingleUnits");
        return singleUnitService.findAll().collectList();
    }

    /**
     * {@code GET  /single-units} : get all the singleUnits as a stream.
     * @return the {@link Flux} of singleUnits.
     */
    @GetMapping(value = "/single-units", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SingleUnitDTO> getAllSingleUnitsAsStream() {
        log.debug("REST request to get all SingleUnits as a stream");
        return singleUnitService.findAll();
    }

    /**
     * {@code GET  /single-units/:id} : get the "id" singleUnit.
     *
     * @param id the id of the singleUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/single-units/{id}")
    public Mono<ResponseEntity<SingleUnitDTO>> getSingleUnit(@PathVariable String id) {
        log.debug("REST request to get SingleUnit : {}", id);
        Mono<SingleUnitDTO> singleUnitDTO = singleUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleUnitDTO);
    }

    /**
     * {@code DELETE  /single-units/:id} : delete the "id" singleUnit.
     *
     * @param id the id of the singleUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/single-units/{id}")
    public Mono<ResponseEntity<Void>> deleteSingleUnit(@PathVariable String id) {
        log.debug("REST request to delete SingleUnit : {}", id);
        return singleUnitService
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
