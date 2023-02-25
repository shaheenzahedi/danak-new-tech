package org.danak.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.danak.backend.IntegrationTest;
import org.danak.backend.domain.Centre;
import org.danak.backend.repository.CentreRepository;
import org.danak.backend.service.dto.CentreDTO;
import org.danak.backend.service.mapper.CentreMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CentreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CentreResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/centres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private CentreMapper centreMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Centre centre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createEntity() {
        Centre centre = new Centre().createTimeStamp(DEFAULT_CREATE_TIME_STAMP).name(DEFAULT_NAME);
        return centre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createUpdatedEntity() {
        Centre centre = new Centre().createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        return centre;
    }

    @BeforeEach
    public void initTest() {
        centreRepository.deleteAll().block();
        centre = createEntity();
    }

    @Test
    void createCentre() throws Exception {
        int databaseSizeBeforeCreate = centreRepository.findAll().collectList().block().size();
        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate + 1);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCentre.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createCentreWithExistingId() throws Exception {
        // Create the Centre with an existing ID
        centre.setId("existing_id");
        CentreDTO centreDTO = centreMapper.toDto(centre);

        int databaseSizeBeforeCreate = centreRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCentresAsStream() {
        // Initialize the database
        centreRepository.save(centre).block();

        List<Centre> centreList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CentreDTO.class)
            .getResponseBody()
            .map(centreMapper::toEntity)
            .filter(centre::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(centreList).isNotNull();
        assertThat(centreList).hasSize(1);
        Centre testCentre = centreList.get(0);
        assertThat(testCentre.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCentre.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllCentres() {
        // Initialize the database
        centreRepository.save(centre).block();

        // Get all the centreList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(centre.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getCentre() {
        // Initialize the database
        centreRepository.save(centre).block();

        // Get the centre
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, centre.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(centre.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingCentre() {
        // Get the centre
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCentre() throws Exception {
        // Initialize the database
        centreRepository.save(centre).block();

        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();

        // Update the centre
        Centre updatedCentre = centreRepository.findById(centre.getId()).block();
        updatedCentre.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        CentreDTO centreDTO = centreMapper.toDto(updatedCentre);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, centreDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCentre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, centreDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCentreWithPatch() throws Exception {
        // Initialize the database
        centreRepository.save(centre).block();

        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();

        // Update the centre using partial update
        Centre partialUpdatedCentre = new Centre();
        partialUpdatedCentre.setId(centre.getId());

        partialUpdatedCentre.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCentre.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCentre))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCentre.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateCentreWithPatch() throws Exception {
        // Initialize the database
        centreRepository.save(centre).block();

        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();

        // Update the centre using partial update
        Centre partialUpdatedCentre = new Centre();
        partialUpdatedCentre.setId(centre.getId());

        partialUpdatedCentre.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCentre.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCentre))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCentre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, centreDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().collectList().block().size();
        centre.setId(UUID.randomUUID().toString());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(centreDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCentre() {
        // Initialize the database
        centreRepository.save(centre).block();

        int databaseSizeBeforeDelete = centreRepository.findAll().collectList().block().size();

        // Delete the centre
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, centre.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Centre> centreList = centreRepository.findAll().collectList().block();
        assertThat(centreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
