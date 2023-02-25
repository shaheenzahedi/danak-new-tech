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
import org.danak.backend.domain.Progress;
import org.danak.backend.repository.ProgressRepository;
import org.danak.backend.service.dto.ProgressDTO;
import org.danak.backend.service.mapper.ProgressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProgressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProgressResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SPENT_TIME = 1L;
    private static final Long UPDATED_SPENT_TIME = 2L;

    private static final String ENTITY_API_URL = "/api/progresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private ProgressMapper progressMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Progress progress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progress createEntity() {
        Progress progress = new Progress().createTimeStamp(DEFAULT_CREATE_TIME_STAMP).spentTime(DEFAULT_SPENT_TIME);
        return progress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progress createUpdatedEntity() {
        Progress progress = new Progress().createTimeStamp(UPDATED_CREATE_TIME_STAMP).spentTime(UPDATED_SPENT_TIME);
        return progress;
    }

    @BeforeEach
    public void initTest() {
        progressRepository.deleteAll().block();
        progress = createEntity();
    }

    @Test
    void createProgress() throws Exception {
        int databaseSizeBeforeCreate = progressRepository.findAll().collectList().block().size();
        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeCreate + 1);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testProgress.getSpentTime()).isEqualTo(DEFAULT_SPENT_TIME);
    }

    @Test
    void createProgressWithExistingId() throws Exception {
        // Create the Progress with an existing ID
        progress.setId("existing_id");
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        int databaseSizeBeforeCreate = progressRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProgressesAsStream() {
        // Initialize the database
        progressRepository.save(progress).block();

        List<Progress> progressList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProgressDTO.class)
            .getResponseBody()
            .map(progressMapper::toEntity)
            .filter(progress::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(progressList).isNotNull();
        assertThat(progressList).hasSize(1);
        Progress testProgress = progressList.get(0);
        assertThat(testProgress.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testProgress.getSpentTime()).isEqualTo(DEFAULT_SPENT_TIME);
    }

    @Test
    void getAllProgresses() {
        // Initialize the database
        progressRepository.save(progress).block();

        // Get all the progressList
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
            .value(hasItem(progress.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].spentTime")
            .value(hasItem(DEFAULT_SPENT_TIME.intValue()));
    }

    @Test
    void getProgress() {
        // Initialize the database
        progressRepository.save(progress).block();

        // Get the progress
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, progress.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(progress.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.spentTime")
            .value(is(DEFAULT_SPENT_TIME.intValue()));
    }

    @Test
    void getNonExistingProgress() {
        // Get the progress
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProgress() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress
        Progress updatedProgress = progressRepository.findById(progress.getId()).block();
        updatedProgress.createTimeStamp(UPDATED_CREATE_TIME_STAMP).spentTime(UPDATED_SPENT_TIME);
        ProgressDTO progressDTO = progressMapper.toDto(updatedProgress);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testProgress.getSpentTime()).isEqualTo(UPDATED_SPENT_TIME);
    }

    @Test
    void putNonExistingProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgressWithPatch() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress using partial update
        Progress partialUpdatedProgress = new Progress();
        partialUpdatedProgress.setId(progress.getId());

        partialUpdatedProgress.spentTime(UPDATED_SPENT_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProgress.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProgress))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testProgress.getSpentTime()).isEqualTo(UPDATED_SPENT_TIME);
    }

    @Test
    void fullUpdateProgressWithPatch() throws Exception {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();

        // Update the progress using partial update
        Progress partialUpdatedProgress = new Progress();
        partialUpdatedProgress.setId(progress.getId());

        partialUpdatedProgress.createTimeStamp(UPDATED_CREATE_TIME_STAMP).spentTime(UPDATED_SPENT_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProgress.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProgress))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
        Progress testProgress = progressList.get(progressList.size() - 1);
        assertThat(testProgress.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testProgress.getSpentTime()).isEqualTo(UPDATED_SPENT_TIME);
    }

    @Test
    void patchNonExistingProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, progressDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProgress() throws Exception {
        int databaseSizeBeforeUpdate = progressRepository.findAll().collectList().block().size();
        progress.setId(UUID.randomUUID().toString());

        // Create the Progress
        ProgressDTO progressDTO = progressMapper.toDto(progress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(progressDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Progress in the database
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProgress() {
        // Initialize the database
        progressRepository.save(progress).block();

        int databaseSizeBeforeDelete = progressRepository.findAll().collectList().block().size();

        // Delete the progress
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, progress.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Progress> progressList = progressRepository.findAll().collectList().block();
        assertThat(progressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
