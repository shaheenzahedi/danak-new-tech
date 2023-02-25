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
import org.danak.backend.domain.Facilitator;
import org.danak.backend.repository.FacilitatorRepository;
import org.danak.backend.service.dto.FacilitatorDTO;
import org.danak.backend.service.mapper.FacilitatorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FacilitatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FacilitatorResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/facilitators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FacilitatorRepository facilitatorRepository;

    @Autowired
    private FacilitatorMapper facilitatorMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Facilitator facilitator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facilitator createEntity() {
        Facilitator facilitator = new Facilitator().createTimeStamp(DEFAULT_CREATE_TIME_STAMP);
        return facilitator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facilitator createUpdatedEntity() {
        Facilitator facilitator = new Facilitator().createTimeStamp(UPDATED_CREATE_TIME_STAMP);
        return facilitator;
    }

    @BeforeEach
    public void initTest() {
        facilitatorRepository.deleteAll().block();
        facilitator = createEntity();
    }

    @Test
    void createFacilitator() throws Exception {
        int databaseSizeBeforeCreate = facilitatorRepository.findAll().collectList().block().size();
        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeCreate + 1);
        Facilitator testFacilitator = facilitatorList.get(facilitatorList.size() - 1);
        assertThat(testFacilitator.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
    }

    @Test
    void createFacilitatorWithExistingId() throws Exception {
        // Create the Facilitator with an existing ID
        facilitator.setId("existing_id");
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        int databaseSizeBeforeCreate = facilitatorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFacilitatorsAsStream() {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        List<Facilitator> facilitatorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FacilitatorDTO.class)
            .getResponseBody()
            .map(facilitatorMapper::toEntity)
            .filter(facilitator::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(facilitatorList).isNotNull();
        assertThat(facilitatorList).hasSize(1);
        Facilitator testFacilitator = facilitatorList.get(0);
        assertThat(testFacilitator.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
    }

    @Test
    void getAllFacilitators() {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        // Get all the facilitatorList
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
            .value(hasItem(facilitator.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()));
    }

    @Test
    void getFacilitator() {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        // Get the facilitator
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, facilitator.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(facilitator.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()));
    }

    @Test
    void getNonExistingFacilitator() {
        // Get the facilitator
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFacilitator() throws Exception {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();

        // Update the facilitator
        Facilitator updatedFacilitator = facilitatorRepository.findById(facilitator.getId()).block();
        updatedFacilitator.createTimeStamp(UPDATED_CREATE_TIME_STAMP);
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(updatedFacilitator);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facilitatorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
        Facilitator testFacilitator = facilitatorList.get(facilitatorList.size() - 1);
        assertThat(testFacilitator.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void putNonExistingFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facilitatorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFacilitatorWithPatch() throws Exception {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();

        // Update the facilitator using partial update
        Facilitator partialUpdatedFacilitator = new Facilitator();
        partialUpdatedFacilitator.setId(facilitator.getId());

        partialUpdatedFacilitator.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacilitator.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilitator))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
        Facilitator testFacilitator = facilitatorList.get(facilitatorList.size() - 1);
        assertThat(testFacilitator.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void fullUpdateFacilitatorWithPatch() throws Exception {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();

        // Update the facilitator using partial update
        Facilitator partialUpdatedFacilitator = new Facilitator();
        partialUpdatedFacilitator.setId(facilitator.getId());

        partialUpdatedFacilitator.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacilitator.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilitator))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
        Facilitator testFacilitator = facilitatorList.get(facilitatorList.size() - 1);
        assertThat(testFacilitator.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void patchNonExistingFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, facilitatorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFacilitator() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorRepository.findAll().collectList().block().size();
        facilitator.setId(UUID.randomUUID().toString());

        // Create the Facilitator
        FacilitatorDTO facilitatorDTO = facilitatorMapper.toDto(facilitator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Facilitator in the database
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFacilitator() {
        // Initialize the database
        facilitatorRepository.save(facilitator).block();

        int databaseSizeBeforeDelete = facilitatorRepository.findAll().collectList().block().size();

        // Delete the facilitator
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, facilitator.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Facilitator> facilitatorList = facilitatorRepository.findAll().collectList().block();
        assertThat(facilitatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
