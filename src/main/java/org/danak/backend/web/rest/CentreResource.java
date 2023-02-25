package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.CentreRepository;
import org.danak.backend.service.CentreService;
import org.danak.backend.service.dto.CentreDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.Centre}.
 */
@RestController
@RequestMapping("/api")
public class CentreResource {

    private final Logger log = LoggerFactory.getLogger(CentreResource.class);

    private static final String ENTITY_NAME = "centre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentreService centreService;

    private final CentreRepository centreRepository;

    public CentreResource(CentreService centreService, CentreRepository centreRepository) {
        this.centreService = centreService;
        this.centreRepository = centreRepository;
    }

    /**
     * {@code POST  /centres} : Create a new centre.
     *
     * @param centreDTO the centreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centreDTO, or with status {@code 400 (Bad Request)} if the centre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/centres")
    public Mono<ResponseEntity<CentreDTO>> createCentre(@RequestBody CentreDTO centreDTO) throws URISyntaxException {
        log.debug("REST request to save Centre : {}", centreDTO);
        if (centreDTO.getId() != null) {
            throw new BadRequestAlertException("A new centre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return centreService
            .save(centreDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/centres/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /centres/:id} : Updates an existing centre.
     *
     * @param id the id of the centreDTO to save.
     * @param centreDTO the centreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreDTO,
     * or with status {@code 400 (Bad Request)} if the centreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/centres/{id}")
    public Mono<ResponseEntity<CentreDTO>> updateCentre(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CentreDTO centreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Centre : {}, {}", id, centreDTO);
        if (centreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return centreRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return centreService
                    .update(centreDTO)
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
     * {@code PATCH  /centres/:id} : Partial updates given fields of an existing centre, field will ignore if it is null
     *
     * @param id the id of the centreDTO to save.
     * @param centreDTO the centreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreDTO,
     * or with status {@code 400 (Bad Request)} if the centreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/centres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CentreDTO>> partialUpdateCentre(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CentreDTO centreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Centre partially : {}, {}", id, centreDTO);
        if (centreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return centreRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CentreDTO> result = centreService.partialUpdate(centreDTO);

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
     * {@code GET  /centres} : get all the centres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centres in body.
     */
    @GetMapping("/centres")
    public Mono<List<CentreDTO>> getAllCentres() {
        log.debug("REST request to get all Centres");
        return centreService.findAll().collectList();
    }

    /**
     * {@code GET  /centres} : get all the centres as a stream.
     * @return the {@link Flux} of centres.
     */
    @GetMapping(value = "/centres", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CentreDTO> getAllCentresAsStream() {
        log.debug("REST request to get all Centres as a stream");
        return centreService.findAll();
    }

    /**
     * {@code GET  /centres/:id} : get the "id" centre.
     *
     * @param id the id of the centreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/centres/{id}")
    public Mono<ResponseEntity<CentreDTO>> getCentre(@PathVariable String id) {
        log.debug("REST request to get Centre : {}", id);
        Mono<CentreDTO> centreDTO = centreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(centreDTO);
    }

    /**
     * {@code DELETE  /centres/:id} : delete the "id" centre.
     *
     * @param id the id of the centreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/centres/{id}")
    public Mono<ResponseEntity<Void>> deleteCentre(@PathVariable String id) {
        log.debug("REST request to delete Centre : {}", id);
        return centreService
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
