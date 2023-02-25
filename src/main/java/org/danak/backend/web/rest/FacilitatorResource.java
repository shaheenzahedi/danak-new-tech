package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.FacilitatorRepository;
import org.danak.backend.service.FacilitatorService;
import org.danak.backend.service.dto.FacilitatorDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.Facilitator}.
 */
@RestController
@RequestMapping("/api")
public class FacilitatorResource {

    private final Logger log = LoggerFactory.getLogger(FacilitatorResource.class);

    private static final String ENTITY_NAME = "facilitator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilitatorService facilitatorService;

    private final FacilitatorRepository facilitatorRepository;

    public FacilitatorResource(FacilitatorService facilitatorService, FacilitatorRepository facilitatorRepository) {
        this.facilitatorService = facilitatorService;
        this.facilitatorRepository = facilitatorRepository;
    }

    /**
     * {@code POST  /facilitators} : Create a new facilitator.
     *
     * @param facilitatorDTO the facilitatorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilitatorDTO, or with status {@code 400 (Bad Request)} if the facilitator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facilitators")
    public Mono<ResponseEntity<FacilitatorDTO>> createFacilitator(@RequestBody FacilitatorDTO facilitatorDTO) throws URISyntaxException {
        log.debug("REST request to save Facilitator : {}", facilitatorDTO);
        if (facilitatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new facilitator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return facilitatorService
            .save(facilitatorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/facilitators/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /facilitators/:id} : Updates an existing facilitator.
     *
     * @param id the id of the facilitatorDTO to save.
     * @param facilitatorDTO the facilitatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilitatorDTO,
     * or with status {@code 400 (Bad Request)} if the facilitatorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilitatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facilitators/{id}")
    public Mono<ResponseEntity<FacilitatorDTO>> updateFacilitator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FacilitatorDTO facilitatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facilitator : {}, {}", id, facilitatorDTO);
        if (facilitatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilitatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facilitatorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return facilitatorService
                    .update(facilitatorDTO)
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
     * {@code PATCH  /facilitators/:id} : Partial updates given fields of an existing facilitator, field will ignore if it is null
     *
     * @param id the id of the facilitatorDTO to save.
     * @param facilitatorDTO the facilitatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilitatorDTO,
     * or with status {@code 400 (Bad Request)} if the facilitatorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facilitatorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilitatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facilitators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FacilitatorDTO>> partialUpdateFacilitator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FacilitatorDTO facilitatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facilitator partially : {}, {}", id, facilitatorDTO);
        if (facilitatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilitatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facilitatorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FacilitatorDTO> result = facilitatorService.partialUpdate(facilitatorDTO);

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
     * {@code GET  /facilitators} : get all the facilitators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilitators in body.
     */
    @GetMapping("/facilitators")
    public Mono<List<FacilitatorDTO>> getAllFacilitators() {
        log.debug("REST request to get all Facilitators");
        return facilitatorService.findAll().collectList();
    }

    /**
     * {@code GET  /facilitators} : get all the facilitators as a stream.
     * @return the {@link Flux} of facilitators.
     */
    @GetMapping(value = "/facilitators", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FacilitatorDTO> getAllFacilitatorsAsStream() {
        log.debug("REST request to get all Facilitators as a stream");
        return facilitatorService.findAll();
    }

    /**
     * {@code GET  /facilitators/:id} : get the "id" facilitator.
     *
     * @param id the id of the facilitatorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilitatorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facilitators/{id}")
    public Mono<ResponseEntity<FacilitatorDTO>> getFacilitator(@PathVariable String id) {
        log.debug("REST request to get Facilitator : {}", id);
        Mono<FacilitatorDTO> facilitatorDTO = facilitatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilitatorDTO);
    }

    /**
     * {@code DELETE  /facilitators/:id} : delete the "id" facilitator.
     *
     * @param id the id of the facilitatorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facilitators/{id}")
    public Mono<ResponseEntity<Void>> deleteFacilitator(@PathVariable String id) {
        log.debug("REST request to delete Facilitator : {}", id);
        return facilitatorService
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
