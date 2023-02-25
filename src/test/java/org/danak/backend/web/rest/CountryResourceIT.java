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
import org.danak.backend.domain.Country;
import org.danak.backend.repository.CountryRepository;
import org.danak.backend.service.dto.CountryDTO;
import org.danak.backend.service.mapper.CountryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CountryResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity() {
        Country country = new Country().createTimeStamp(DEFAULT_CREATE_TIME_STAMP).name(DEFAULT_NAME);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity() {
        Country country = new Country().createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        return country;
    }

    @BeforeEach
    public void initTest() {
        countryRepository.deleteAll().block();
        country = createEntity();
    }

    @Test
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().collectList().block().size();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId("existing_id");
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCountriesAsStream() {
        // Initialize the database
        countryRepository.save(country).block();

        List<Country> countryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CountryDTO.class)
            .getResponseBody()
            .map(countryMapper::toEntity)
            .filter(country::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(countryList).isNotNull();
        assertThat(countryList).hasSize(1);
        Country testCountry = countryList.get(0);
        assertThat(testCountry.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllCountries() {
        // Initialize the database
        countryRepository.save(country).block();

        // Get all the countryList
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
            .value(hasItem(country.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getCountry() {
        // Initialize the database
        countryRepository.save(country).block();

        // Get the country
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, country.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(country.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingCountry() {
        // Get the country
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCountry() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).block();
        updatedCountry.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.createTimeStamp(UPDATED_CREATE_TIME_STAMP).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        country.setId(UUID.randomUUID().toString());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCountry() {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeDelete = countryRepository.findAll().collectList().block().size();

        // Delete the country
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, country.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
