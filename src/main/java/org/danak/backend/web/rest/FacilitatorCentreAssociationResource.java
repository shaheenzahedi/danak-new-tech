package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.FacilitatorCentreAssociationRepository;
import org.danak.backend.service.FacilitatorCentreAssociationService;
import org.danak.backend.service.dto.FacilitatorCentreAssociationDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.FacilitatorCentreAssociation}.
 */
@RestController
@RequestMapping("/api")
public class FacilitatorCentreAssociationResource {

    private final Logger log = LoggerFactory.getLogger(FacilitatorCentreAssociationResource.class);

    private static final String ENTITY_NAME = "facilitatorCentreAssociation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilitatorCentreAssociationService facilitatorCentreAssociationService;

    private final FacilitatorCentreAssociationRepository facilitatorCentreAssociationRepository;

    public FacilitatorCentreAssociationResource(
        FacilitatorCentreAssociationService facilitatorCentreAssociationService,
        FacilitatorCentreAssociationRepository facilitatorCentreAssociationRepository
    ) {
        this.facilitatorCentreAssociationService = facilitatorCentreAssociationService;
        this.facilitatorCentreAssociationRepository = facilitatorCentreAssociationRepository;
    }

    /**
     * {@code POST  /facilitator-centre-associations} : Create a new facilitatorCentreAssociation.
     *
     * @param facilitatorCentreAssociationDTO the facilitatorCentreAssociationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilitatorCentreAssociationDTO, or with status {@code 400 (Bad Request)} if the facilitatorCentreAssociation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facilitator-centre-associations")
    public Mono<ResponseEntity<FacilitatorCentreAssociationDTO>> createFacilitatorCentreAssociation(
        @RequestBody FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FacilitatorCentreAssociation : {}", facilitatorCentreAssociationDTO);
        if (facilitatorCentreAssociationDTO.getId() != null) {
            throw new BadRequestAlertException("A new facilitatorCentreAssociation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return facilitatorCentreAssociationService
            .save(facilitatorCentreAssociationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/facilitator-centre-associations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /facilitator-centre-associations/:id} : Updates an existing facilitatorCentreAssociation.
     *
     * @param id the id of the facilitatorCentreAssociationDTO to save.
     * @param facilitatorCentreAssociationDTO the facilitatorCentreAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilitatorCentreAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the facilitatorCentreAssociationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilitatorCentreAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facilitator-centre-associations/{id}")
    public Mono<ResponseEntity<FacilitatorCentreAssociationDTO>> updateFacilitatorCentreAssociation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacilitatorCentreAssociation : {}, {}", id, facilitatorCentreAssociationDTO);
        if (facilitatorCentreAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilitatorCentreAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facilitatorCentreAssociationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return facilitatorCentreAssociationService
                    .update(facilitatorCentreAssociationDTO)
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
     * {@code PATCH  /facilitator-centre-associations/:id} : Partial updates given fields of an existing facilitatorCentreAssociation, field will ignore if it is null
     *
     * @param id the id of the facilitatorCentreAssociationDTO to save.
     * @param facilitatorCentreAssociationDTO the facilitatorCentreAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilitatorCentreAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the facilitatorCentreAssociationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facilitatorCentreAssociationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilitatorCentreAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facilitator-centre-associations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FacilitatorCentreAssociationDTO>> partialUpdateFacilitatorCentreAssociation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacilitatorCentreAssociation partially : {}, {}", id, facilitatorCentreAssociationDTO);
        if (facilitatorCentreAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilitatorCentreAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facilitatorCentreAssociationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FacilitatorCentreAssociationDTO> result = facilitatorCentreAssociationService.partialUpdate(
                    facilitatorCentreAssociationDTO
                );

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
     * {@code GET  /facilitator-centre-associations} : get all the facilitatorCentreAssociations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilitatorCentreAssociations in body.
     */
    @GetMapping("/facilitator-centre-associations")
    public Mono<List<FacilitatorCentreAssociationDTO>> getAllFacilitatorCentreAssociations() {
        log.debug("REST request to get all FacilitatorCentreAssociations");
        return facilitatorCentreAssociationService.findAll().collectList();
    }

    /**
     * {@code GET  /facilitator-centre-associations} : get all the facilitatorCentreAssociations as a stream.
     * @return the {@link Flux} of facilitatorCentreAssociations.
     */
    @GetMapping(value = "/facilitator-centre-associations", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FacilitatorCentreAssociationDTO> getAllFacilitatorCentreAssociationsAsStream() {
        log.debug("REST request to get all FacilitatorCentreAssociations as a stream");
        return facilitatorCentreAssociationService.findAll();
    }

    /**
     * {@code GET  /facilitator-centre-associations/:id} : get the "id" facilitatorCentreAssociation.
     *
     * @param id the id of the facilitatorCentreAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilitatorCentreAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facilitator-centre-associations/{id}")
    public Mono<ResponseEntity<FacilitatorCentreAssociationDTO>> getFacilitatorCentreAssociation(@PathVariable String id) {
        log.debug("REST request to get FacilitatorCentreAssociation : {}", id);
        Mono<FacilitatorCentreAssociationDTO> facilitatorCentreAssociationDTO = facilitatorCentreAssociationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilitatorCentreAssociationDTO);
    }

    /**
     * {@code DELETE  /facilitator-centre-associations/:id} : delete the "id" facilitatorCentreAssociation.
     *
     * @param id the id of the facilitatorCentreAssociationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facilitator-centre-associations/{id}")
    public Mono<ResponseEntity<Void>> deleteFacilitatorCentreAssociation(@PathVariable String id) {
        log.debug("REST request to delete FacilitatorCentreAssociation : {}", id);
        return facilitatorCentreAssociationService
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
