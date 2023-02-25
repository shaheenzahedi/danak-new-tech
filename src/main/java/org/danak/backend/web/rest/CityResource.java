package org.danak.backend.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.danak.backend.repository.CityRepository;
import org.danak.backend.service.CityService;
import org.danak.backend.service.dto.CityDTO;
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
 * REST controller for managing {@link org.danak.backend.domain.City}.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityService cityService;

    private final CityRepository cityRepository;

    public CityResource(CityService cityService, CityRepository cityRepository) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
    }

    /**
     * {@code POST  /cities} : Create a new city.
     *
     * @param cityDTO the cityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cityDTO, or with status {@code 400 (Bad Request)} if the city has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cities")
    public Mono<ResponseEntity<CityDTO>> createCity(@RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to save City : {}", cityDTO);
        if (cityDTO.getId() != null) {
            throw new BadRequestAlertException("A new city cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cityService
            .save(cityDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cities/:id} : Updates an existing city.
     *
     * @param id the id of the cityDTO to save.
     * @param cityDTO the cityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDTO,
     * or with status {@code 400 (Bad Request)} if the cityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cities/{id}")
    public Mono<ResponseEntity<CityDTO>> updateCity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CityDTO cityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update City : {}, {}", id, cityDTO);
        if (cityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cityService
                    .update(cityDTO)
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
     * {@code PATCH  /cities/:id} : Partial updates given fields of an existing city, field will ignore if it is null
     *
     * @param id the id of the cityDTO to save.
     * @param cityDTO the cityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDTO,
     * or with status {@code 400 (Bad Request)} if the cityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CityDTO>> partialUpdateCity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CityDTO cityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update City partially : {}, {}", id, cityDTO);
        if (cityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CityDTO> result = cityService.partialUpdate(cityDTO);

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
     * {@code GET  /cities} : get all the cities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cities in body.
     */
    @GetMapping("/cities")
    public Mono<List<CityDTO>> getAllCities() {
        log.debug("REST request to get all Cities");
        return cityService.findAll().collectList();
    }

    /**
     * {@code GET  /cities} : get all the cities as a stream.
     * @return the {@link Flux} of cities.
     */
    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CityDTO> getAllCitiesAsStream() {
        log.debug("REST request to get all Cities as a stream");
        return cityService.findAll();
    }

    /**
     * {@code GET  /cities/:id} : get the "id" city.
     *
     * @param id the id of the cityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cities/{id}")
    public Mono<ResponseEntity<CityDTO>> getCity(@PathVariable String id) {
        log.debug("REST request to get City : {}", id);
        Mono<CityDTO> cityDTO = cityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cityDTO);
    }

    /**
     * {@code DELETE  /cities/:id} : delete the "id" city.
     *
     * @param id the id of the cityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cities/{id}")
    public Mono<ResponseEntity<Void>> deleteCity(@PathVariable String id) {
        log.debug("REST request to delete City : {}", id);
        return cityService
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
