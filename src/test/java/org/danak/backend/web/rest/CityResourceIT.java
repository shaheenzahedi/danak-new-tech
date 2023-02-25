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
import org.danak.backend.domain.City;
import org.danak.backend.repository.CityRepository;
import org.danak.backend.service.dto.CityDTO;
import org.danak.backend.service.mapper.CityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CityResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VILLAGE = false;
    private static final Boolean UPDATED_IS_VILLAGE = true;

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private WebTestClient webTestClient;

    private City city;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity() {
        City city = new City().createTimeStamp(DEFAULT_CREATE_TIME_STAMP).name(DEFAULT_NAME).isVillage(DEFAULT_IS_VILLAGE);
        return city;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity() {
        City city = new City().createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME).isVillage(UPDATED_IS_VILLAGE);
        return city;
    }

    @BeforeEach
    public void initTest() {
        cityRepository.deleteAll().block();
        city = createEntity();
    }

    @Test
    void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().collectList().block().size();
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getIsVillage()).isEqualTo(DEFAULT_IS_VILLAGE);
    }

    @Test
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        city.setId("existing_id");
        CityDTO cityDTO = cityMapper.toDto(city);

        int databaseSizeBeforeCreate = cityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCitiesAsStream() {
        // Initialize the database
        cityRepository.save(city).block();

        List<City> cityList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CityDTO.class)
            .getResponseBody()
            .map(cityMapper::toEntity)
            .filter(city::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cityList).isNotNull();
        assertThat(cityList).hasSize(1);
        City testCity = cityList.get(0);
        assertThat(testCity.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getIsVillage()).isEqualTo(DEFAULT_IS_VILLAGE);
    }

    @Test
    void getAllCities() {
        // Initialize the database
        cityRepository.save(city).block();

        // Get all the cityList
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
            .value(hasItem(city.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].isVillage")
            .value(hasItem(DEFAULT_IS_VILLAGE.booleanValue()));
    }

    @Test
    void getCity() {
        // Initialize the database
        cityRepository.save(city).block();

        // Get the city
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, city.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(city.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.isVillage")
            .value(is(DEFAULT_IS_VILLAGE.booleanValue()));
    }

    @Test
    void getNonExistingCity() {
        // Get the city
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCity() throws Exception {
        // Initialize the database
        cityRepository.save(city).block();

        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).block();
        updatedCity.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME).isVillage(UPDATED_IS_VILLAGE);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getIsVillage()).isEqualTo(UPDATED_IS_VILLAGE);
    }

    @Test
    void putNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.save(city).block();

        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getIsVillage()).isEqualTo(DEFAULT_IS_VILLAGE);
    }

    @Test
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        cityRepository.save(city).block();

        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME).isVillage(UPDATED_IS_VILLAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
        City testCity = cityList.get(cityList.size() - 1);
        assertThat(testCity.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getIsVillage()).isEqualTo(UPDATED_IS_VILLAGE);
    }

    @Test
    void patchNonExistingCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCity() throws Exception {
        int databaseSizeBeforeUpdate = cityRepository.findAll().collectList().block().size();
        city.setId(UUID.randomUUID().toString());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the City in the database
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCity() {
        // Initialize the database
        cityRepository.save(city).block();

        int databaseSizeBeforeDelete = cityRepository.findAll().collectList().block().size();

        // Delete the city
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, city.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<City> cityList = cityRepository.findAll().collectList().block();
        assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
