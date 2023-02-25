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
import org.danak.backend.domain.Child;
import org.danak.backend.repository.ChildRepository;
import org.danak.backend.service.dto.ChildDTO;
import org.danak.backend.service.mapper.ChildMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChildResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Child child;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createEntity() {
        Child child = new Child().createTimeStamp(DEFAULT_CREATE_TIME_STAMP);
        return child;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createUpdatedEntity() {
        Child child = new Child().createTimeStamp(UPDATED_CREATE_TIME_STAMP);
        return child;
    }

    @BeforeEach
    public void initTest() {
        childRepository.deleteAll().block();
        child = createEntity();
    }

    @Test
    void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().collectList().block().size();
        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
    }

    @Test
    void createChildWithExistingId() throws Exception {
        // Create the Child with an existing ID
        child.setId("existing_id");
        ChildDTO childDTO = childMapper.toDto(child);

        int databaseSizeBeforeCreate = childRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllChildrenAsStream() {
        // Initialize the database
        childRepository.save(child).block();

        List<Child> childList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ChildDTO.class)
            .getResponseBody()
            .map(childMapper::toEntity)
            .filter(child::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(childList).isNotNull();
        assertThat(childList).hasSize(1);
        Child testChild = childList.get(0);
        assertThat(testChild.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
    }

    @Test
    void getAllChildren() {
        // Initialize the database
        childRepository.save(child).block();

        // Get all the childList
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
            .value(hasItem(child.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()));
    }

    @Test
    void getChild() {
        // Initialize the database
        childRepository.save(child).block();

        // Get the child
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, child.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(child.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()));
    }

    @Test
    void getNonExistingChild() {
        // Get the child
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewChild() throws Exception {
        // Initialize the database
        childRepository.save(child).block();

        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).block();
        updatedChild.createTimeStamp(UPDATED_CREATE_TIME_STAMP);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, childDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void putNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, childDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.save(child).block();

        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChild.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void fullUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.save(child).block();

        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChild.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    void patchNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, childDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().collectList().block().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(childDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChild() {
        // Initialize the database
        childRepository.save(child).block();

        int databaseSizeBeforeDelete = childRepository.findAll().collectList().block().size();

        // Delete the child
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, child.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Child> childList = childRepository.findAll().collectList().block();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
