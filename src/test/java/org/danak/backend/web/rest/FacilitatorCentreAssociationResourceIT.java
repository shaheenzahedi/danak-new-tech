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
import org.danak.backend.domain.FacilitatorCentreAssociation;
import org.danak.backend.repository.FacilitatorCentreAssociationRepository;
import org.danak.backend.service.dto.FacilitatorCentreAssociationDTO;
import org.danak.backend.service.mapper.FacilitatorCentreAssociationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FacilitatorCentreAssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FacilitatorCentreAssociationResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_JOIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/facilitator-centre-associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FacilitatorCentreAssociationRepository facilitatorCentreAssociationRepository;

    @Autowired
    private FacilitatorCentreAssociationMapper facilitatorCentreAssociationMapper;

    @Autowired
    private WebTestClient webTestClient;

    private FacilitatorCentreAssociation facilitatorCentreAssociation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilitatorCentreAssociation createEntity() {
        FacilitatorCentreAssociation facilitatorCentreAssociation = new FacilitatorCentreAssociation()
            .createTimeStamp(DEFAULT_CREATE_TIME_STAMP)
            .joinDate(DEFAULT_JOIN_DATE);
        return facilitatorCentreAssociation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilitatorCentreAssociation createUpdatedEntity() {
        FacilitatorCentreAssociation facilitatorCentreAssociation = new FacilitatorCentreAssociation()
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .joinDate(UPDATED_JOIN_DATE);
        return facilitatorCentreAssociation;
    }

    @BeforeEach
    public void initTest() {
        facilitatorCentreAssociationRepository.deleteAll().block();
        facilitatorCentreAssociation = createEntity();
    }

    @Test
    void createFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeCreate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeCreate + 1);
        FacilitatorCentreAssociation testFacilitatorCentreAssociation = facilitatorCentreAssociationList.get(
            facilitatorCentreAssociationList.size() - 1
        );
        assertThat(testFacilitatorCentreAssociation.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testFacilitatorCentreAssociation.getJoinDate()).isEqualTo(DEFAULT_JOIN_DATE);
    }

    @Test
    void createFacilitatorCentreAssociationWithExistingId() throws Exception {
        // Create the FacilitatorCentreAssociation with an existing ID
        facilitatorCentreAssociation.setId("existing_id");
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        int databaseSizeBeforeCreate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFacilitatorCentreAssociationsAsStream() {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FacilitatorCentreAssociationDTO.class)
            .getResponseBody()
            .map(facilitatorCentreAssociationMapper::toEntity)
            .filter(facilitatorCentreAssociation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(facilitatorCentreAssociationList).isNotNull();
        assertThat(facilitatorCentreAssociationList).hasSize(1);
        FacilitatorCentreAssociation testFacilitatorCentreAssociation = facilitatorCentreAssociationList.get(0);
        assertThat(testFacilitatorCentreAssociation.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testFacilitatorCentreAssociation.getJoinDate()).isEqualTo(DEFAULT_JOIN_DATE);
    }

    @Test
    void getAllFacilitatorCentreAssociations() {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        // Get all the facilitatorCentreAssociationList
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
            .value(hasItem(facilitatorCentreAssociation.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].joinDate")
            .value(hasItem(DEFAULT_JOIN_DATE.toString()));
    }

    @Test
    void getFacilitatorCentreAssociation() {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        // Get the facilitatorCentreAssociation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, facilitatorCentreAssociation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(facilitatorCentreAssociation.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.joinDate")
            .value(is(DEFAULT_JOIN_DATE.toString()));
    }

    @Test
    void getNonExistingFacilitatorCentreAssociation() {
        // Get the facilitatorCentreAssociation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFacilitatorCentreAssociation() throws Exception {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();

        // Update the facilitatorCentreAssociation
        FacilitatorCentreAssociation updatedFacilitatorCentreAssociation = facilitatorCentreAssociationRepository
            .findById(facilitatorCentreAssociation.getId())
            .block();
        updatedFacilitatorCentreAssociation.createTimeStamp(UPDATED_CREATE_TIME_STAMP).joinDate(UPDATED_JOIN_DATE);
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            updatedFacilitatorCentreAssociation
        );

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facilitatorCentreAssociationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
        FacilitatorCentreAssociation testFacilitatorCentreAssociation = facilitatorCentreAssociationList.get(
            facilitatorCentreAssociationList.size() - 1
        );
        assertThat(testFacilitatorCentreAssociation.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testFacilitatorCentreAssociation.getJoinDate()).isEqualTo(UPDATED_JOIN_DATE);
    }

    @Test
    void putNonExistingFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facilitatorCentreAssociationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFacilitatorCentreAssociationWithPatch() throws Exception {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();

        // Update the facilitatorCentreAssociation using partial update
        FacilitatorCentreAssociation partialUpdatedFacilitatorCentreAssociation = new FacilitatorCentreAssociation();
        partialUpdatedFacilitatorCentreAssociation.setId(facilitatorCentreAssociation.getId());

        partialUpdatedFacilitatorCentreAssociation.createTimeStamp(UPDATED_CREATE_TIME_STAMP).joinDate(UPDATED_JOIN_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacilitatorCentreAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilitatorCentreAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
        FacilitatorCentreAssociation testFacilitatorCentreAssociation = facilitatorCentreAssociationList.get(
            facilitatorCentreAssociationList.size() - 1
        );
        assertThat(testFacilitatorCentreAssociation.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testFacilitatorCentreAssociation.getJoinDate()).isEqualTo(UPDATED_JOIN_DATE);
    }

    @Test
    void fullUpdateFacilitatorCentreAssociationWithPatch() throws Exception {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();

        // Update the facilitatorCentreAssociation using partial update
        FacilitatorCentreAssociation partialUpdatedFacilitatorCentreAssociation = new FacilitatorCentreAssociation();
        partialUpdatedFacilitatorCentreAssociation.setId(facilitatorCentreAssociation.getId());

        partialUpdatedFacilitatorCentreAssociation.createTimeStamp(UPDATED_CREATE_TIME_STAMP).joinDate(UPDATED_JOIN_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacilitatorCentreAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilitatorCentreAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
        FacilitatorCentreAssociation testFacilitatorCentreAssociation = facilitatorCentreAssociationList.get(
            facilitatorCentreAssociationList.size() - 1
        );
        assertThat(testFacilitatorCentreAssociation.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testFacilitatorCentreAssociation.getJoinDate()).isEqualTo(UPDATED_JOIN_DATE);
    }

    @Test
    void patchNonExistingFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, facilitatorCentreAssociationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFacilitatorCentreAssociation() throws Exception {
        int databaseSizeBeforeUpdate = facilitatorCentreAssociationRepository.findAll().collectList().block().size();
        facilitatorCentreAssociation.setId(UUID.randomUUID().toString());

        // Create the FacilitatorCentreAssociation
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = facilitatorCentreAssociationMapper.toDto(
            facilitatorCentreAssociation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facilitatorCentreAssociationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FacilitatorCentreAssociation in the database
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFacilitatorCentreAssociation() {
        // Initialize the database
        facilitatorCentreAssociationRepository.save(facilitatorCentreAssociation).block();

        int databaseSizeBeforeDelete = facilitatorCentreAssociationRepository.findAll().collectList().block().size();

        // Delete the facilitatorCentreAssociation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, facilitatorCentreAssociation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FacilitatorCentreAssociation> facilitatorCentreAssociationList = facilitatorCentreAssociationRepository
            .findAll()
            .collectList()
            .block();
        assertThat(facilitatorCentreAssociationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
