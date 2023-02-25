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
import org.danak.backend.domain.Province;
import org.danak.backend.repository.ProvinceRepository;
import org.danak.backend.service.dto.ProvinceDTO;
import org.danak.backend.service.mapper.ProvinceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProvinceResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity() {
        Province province = new Province().createTimeStamp(DEFAULT_CREATE_TIME_STAMP).name(DEFAULT_NAME);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity() {
        Province province = new Province().createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        return province;
    }

    @BeforeEach
    public void initTest() {
        provinceRepository.deleteAll().block();
        province = createEntity();
    }

    @Test
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().collectList().block().size();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId("existing_id");
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        int databaseSizeBeforeCreate = provinceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProvincesAsStream() {
        // Initialize the database
        provinceRepository.save(province).block();

        List<Province> provinceList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProvinceDTO.class)
            .getResponseBody()
            .map(provinceMapper::toEntity)
            .filter(province::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(provinceList).isNotNull();
        assertThat(provinceList).hasSize(1);
        Province testProvince = provinceList.get(0);
        assertThat(testProvince.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllProvinces() {
        // Initialize the database
        provinceRepository.save(province).block();

        // Get all the provinceList
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
            .value(hasItem(province.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getProvince() {
        // Initialize the database
        provinceRepository.save(province).block();

        // Get the province
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, province.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(province.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingProvince() {
        // Get the province
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProvince() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).block();
        updatedProvince.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(UUID.randomUUID().toString());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProvince() {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeDelete = provinceRepository.findAll().collectList().block().size();

        // Delete the province
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, province.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
