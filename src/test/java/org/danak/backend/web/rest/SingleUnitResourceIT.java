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
import org.danak.backend.domain.SingleUnit;
import org.danak.backend.repository.SingleUnitRepository;
import org.danak.backend.service.dto.SingleUnitDTO;
import org.danak.backend.service.mapper.SingleUnitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SingleUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SingleUnitResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_GLOBAL_NUM = "AAAAAAAAAA";
    private static final String UPDATED_GLOBAL_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_TARGET = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final String DEFAULT_WORDS = "AAAAAAAAAA";
    private static final String UPDATED_WORDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/single-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SingleUnitRepository singleUnitRepository;

    @Autowired
    private SingleUnitMapper singleUnitMapper;

    @Autowired
    private WebTestClient webTestClient;

    private SingleUnit singleUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleUnit createEntity() {
        SingleUnit singleUnit = new SingleUnit()
            .createTimeStamp(DEFAULT_CREATE_TIME_STAMP)
            .globalNum(DEFAULT_GLOBAL_NUM)
            .icon(DEFAULT_ICON)
            .target(DEFAULT_TARGET)
            .params(DEFAULT_PARAMS)
            .words(DEFAULT_WORDS);
        return singleUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleUnit createUpdatedEntity() {
        SingleUnit singleUnit = new SingleUnit()
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .globalNum(UPDATED_GLOBAL_NUM)
            .icon(UPDATED_ICON)
            .target(UPDATED_TARGET)
            .params(UPDATED_PARAMS)
            .words(UPDATED_WORDS);
        return singleUnit;
    }

    @BeforeEach
    public void initTest() {
        singleUnitRepository.deleteAll().block();
        singleUnit = createEntity();
    }

    @Test
    void createSingleUnit() throws Exception {
        int databaseSizeBeforeCreate = singleUnitRepository.findAll().collectList().block().size();
        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeCreate + 1);
        SingleUnit testSingleUnit = singleUnitList.get(singleUnitList.size() - 1);
        assertThat(testSingleUnit.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testSingleUnit.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testSingleUnit.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSingleUnit.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testSingleUnit.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSingleUnit.getWords()).isEqualTo(DEFAULT_WORDS);
    }

    @Test
    void createSingleUnitWithExistingId() throws Exception {
        // Create the SingleUnit with an existing ID
        singleUnit.setId("existing_id");
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        int databaseSizeBeforeCreate = singleUnitRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSingleUnitsAsStream() {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        List<SingleUnit> singleUnitList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SingleUnitDTO.class)
            .getResponseBody()
            .map(singleUnitMapper::toEntity)
            .filter(singleUnit::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(singleUnitList).isNotNull();
        assertThat(singleUnitList).hasSize(1);
        SingleUnit testSingleUnit = singleUnitList.get(0);
        assertThat(testSingleUnit.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testSingleUnit.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testSingleUnit.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSingleUnit.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testSingleUnit.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSingleUnit.getWords()).isEqualTo(DEFAULT_WORDS);
    }

    @Test
    void getAllSingleUnits() {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        // Get all the singleUnitList
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
            .value(hasItem(singleUnit.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].globalNum")
            .value(hasItem(DEFAULT_GLOBAL_NUM))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].target")
            .value(hasItem(DEFAULT_TARGET))
            .jsonPath("$.[*].params")
            .value(hasItem(DEFAULT_PARAMS))
            .jsonPath("$.[*].words")
            .value(hasItem(DEFAULT_WORDS));
    }

    @Test
    void getSingleUnit() {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        // Get the singleUnit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singleUnit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singleUnit.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.globalNum")
            .value(is(DEFAULT_GLOBAL_NUM))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.target")
            .value(is(DEFAULT_TARGET))
            .jsonPath("$.params")
            .value(is(DEFAULT_PARAMS))
            .jsonPath("$.words")
            .value(is(DEFAULT_WORDS));
    }

    @Test
    void getNonExistingSingleUnit() {
        // Get the singleUnit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSingleUnit() throws Exception {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();

        // Update the singleUnit
        SingleUnit updatedSingleUnit = singleUnitRepository.findById(singleUnit.getId()).block();
        updatedSingleUnit
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .globalNum(UPDATED_GLOBAL_NUM)
            .icon(UPDATED_ICON)
            .target(UPDATED_TARGET)
            .params(UPDATED_PARAMS)
            .words(UPDATED_WORDS);
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(updatedSingleUnit);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleUnitDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
        SingleUnit testSingleUnit = singleUnitList.get(singleUnitList.size() - 1);
        assertThat(testSingleUnit.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testSingleUnit.getGlobalNum()).isEqualTo(UPDATED_GLOBAL_NUM);
        assertThat(testSingleUnit.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSingleUnit.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testSingleUnit.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSingleUnit.getWords()).isEqualTo(UPDATED_WORDS);
    }

    @Test
    void putNonExistingSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleUnitDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSingleUnitWithPatch() throws Exception {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();

        // Update the singleUnit using partial update
        SingleUnit partialUpdatedSingleUnit = new SingleUnit();
        partialUpdatedSingleUnit.setId(singleUnit.getId());

        partialUpdatedSingleUnit
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .target(UPDATED_TARGET)
            .params(UPDATED_PARAMS)
            .words(UPDATED_WORDS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleUnit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleUnit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
        SingleUnit testSingleUnit = singleUnitList.get(singleUnitList.size() - 1);
        assertThat(testSingleUnit.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testSingleUnit.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testSingleUnit.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSingleUnit.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testSingleUnit.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSingleUnit.getWords()).isEqualTo(UPDATED_WORDS);
    }

    @Test
    void fullUpdateSingleUnitWithPatch() throws Exception {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();

        // Update the singleUnit using partial update
        SingleUnit partialUpdatedSingleUnit = new SingleUnit();
        partialUpdatedSingleUnit.setId(singleUnit.getId());

        partialUpdatedSingleUnit
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .globalNum(UPDATED_GLOBAL_NUM)
            .icon(UPDATED_ICON)
            .target(UPDATED_TARGET)
            .params(UPDATED_PARAMS)
            .words(UPDATED_WORDS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleUnit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleUnit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
        SingleUnit testSingleUnit = singleUnitList.get(singleUnitList.size() - 1);
        assertThat(testSingleUnit.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testSingleUnit.getGlobalNum()).isEqualTo(UPDATED_GLOBAL_NUM);
        assertThat(testSingleUnit.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSingleUnit.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testSingleUnit.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSingleUnit.getWords()).isEqualTo(UPDATED_WORDS);
    }

    @Test
    void patchNonExistingSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singleUnitDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSingleUnit() throws Exception {
        int databaseSizeBeforeUpdate = singleUnitRepository.findAll().collectList().block().size();
        singleUnit.setId(UUID.randomUUID().toString());

        // Create the SingleUnit
        SingleUnitDTO singleUnitDTO = singleUnitMapper.toDto(singleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleUnitDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleUnit in the database
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSingleUnit() {
        // Initialize the database
        singleUnitRepository.save(singleUnit).block();

        int databaseSizeBeforeDelete = singleUnitRepository.findAll().collectList().block().size();

        // Delete the singleUnit
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singleUnit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SingleUnit> singleUnitList = singleUnitRepository.findAll().collectList().block();
        assertThat(singleUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
