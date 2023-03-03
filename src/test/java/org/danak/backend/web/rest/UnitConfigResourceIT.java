package org.danak.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.danak.backend.IntegrationTest;
import org.danak.backend.domain.UnitConfig;
import org.danak.backend.repository.UnitConfigRepository;
import org.danak.backend.service.dto.UnitConfigDTO;
import org.danak.backend.service.mapper.UnitConfigMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UnitConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UnitConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unit-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UnitConfigRepository unitConfigRepository;

    @Autowired
    private UnitConfigMapper unitConfigMapper;

    @Autowired
    private WebTestClient webTestClient;

    private UnitConfig unitConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitConfig createEntity() {
        UnitConfig unitConfig = new UnitConfig().name(DEFAULT_NAME).displayName(DEFAULT_DISPLAY_NAME);
        return unitConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitConfig createUpdatedEntity() {
        UnitConfig unitConfig = new UnitConfig().name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);
        return unitConfig;
    }

    @BeforeEach
    public void initTest() {
        unitConfigRepository.deleteAll().block();
        unitConfig = createEntity();
    }

    @Test
    void createUnitConfig() throws Exception {
        int databaseSizeBeforeCreate = unitConfigRepository.findAll().collectList().block().size();
        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeCreate + 1);
        UnitConfig testUnitConfig = unitConfigList.get(unitConfigList.size() - 1);
        assertThat(testUnitConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitConfig.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
    }

    @Test
    void createUnitConfigWithExistingId() throws Exception {
        // Create the UnitConfig with an existing ID
        unitConfig.setId("existing_id");
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        int databaseSizeBeforeCreate = unitConfigRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUnitConfigsAsStream() {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        List<UnitConfig> unitConfigList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UnitConfigDTO.class)
            .getResponseBody()
            .map(unitConfigMapper::toEntity)
            .filter(unitConfig::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(unitConfigList).isNotNull();
        assertThat(unitConfigList).hasSize(1);
        UnitConfig testUnitConfig = unitConfigList.get(0);
        assertThat(testUnitConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitConfig.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
    }

    @Test
    void getAllUnitConfigs() {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        // Get all the unitConfigList
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
            .value(hasItem(unitConfig.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME));
    }

    @Test
    void getUnitConfig() {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        // Get the unitConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, unitConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(unitConfig.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME));
    }

    @Test
    void getNonExistingUnitConfig() {
        // Get the unitConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUnitConfig() throws Exception {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();

        // Update the unitConfig
        UnitConfig updatedUnitConfig = unitConfigRepository.findById(unitConfig.getId()).block();
        updatedUnitConfig.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(updatedUnitConfig);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, unitConfigDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
        UnitConfig testUnitConfig = unitConfigList.get(unitConfigList.size() - 1);
        assertThat(testUnitConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitConfig.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void putNonExistingUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, unitConfigDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUnitConfigWithPatch() throws Exception {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();

        // Update the unitConfig using partial update
        UnitConfig partialUpdatedUnitConfig = new UnitConfig();
        partialUpdatedUnitConfig.setId(unitConfig.getId());

        partialUpdatedUnitConfig.displayName(UPDATED_DISPLAY_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUnitConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
        UnitConfig testUnitConfig = unitConfigList.get(unitConfigList.size() - 1);
        assertThat(testUnitConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitConfig.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void fullUpdateUnitConfigWithPatch() throws Exception {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();

        // Update the unitConfig using partial update
        UnitConfig partialUpdatedUnitConfig = new UnitConfig();
        partialUpdatedUnitConfig.setId(unitConfig.getId());

        partialUpdatedUnitConfig.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUnitConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
        UnitConfig testUnitConfig = unitConfigList.get(unitConfigList.size() - 1);
        assertThat(testUnitConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitConfig.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void patchNonExistingUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, unitConfigDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUnitConfig() throws Exception {
        int databaseSizeBeforeUpdate = unitConfigRepository.findAll().collectList().block().size();
        unitConfig.setId(UUID.randomUUID().toString());

        // Create the UnitConfig
        UnitConfigDTO unitConfigDTO = unitConfigMapper.toDto(unitConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitConfigDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UnitConfig in the database
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUnitConfig() {
        // Initialize the database
        unitConfigRepository.save(unitConfig).block();

        int databaseSizeBeforeDelete = unitConfigRepository.findAll().collectList().block().size();

        // Delete the unitConfig
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, unitConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UnitConfig> unitConfigList = unitConfigRepository.findAll().collectList().block();
        assertThat(unitConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
