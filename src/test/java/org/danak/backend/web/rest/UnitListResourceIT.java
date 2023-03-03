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
import org.danak.backend.domain.UnitList;
import org.danak.backend.domain.enumeration.PresenterName;
import org.danak.backend.domain.enumeration.UnitListType;
import org.danak.backend.repository.UnitListRepository;
import org.danak.backend.service.dto.UnitListDTO;
import org.danak.backend.service.mapper.UnitListMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UnitListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UnitListResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer UPDATED_NUM = 2;

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final UnitListType DEFAULT_TYPE = UnitListType.STUDY;
    private static final UnitListType UPDATED_TYPE = UnitListType.REVIEW;

    private static final PresenterName DEFAULT_PRESENTER = PresenterName.SAM;
    private static final PresenterName UPDATED_PRESENTER = PresenterName.SARA;

    private static final String ENTITY_API_URL = "/api/unit-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UnitListRepository unitListRepository;

    @Autowired
    private UnitListMapper unitListMapper;

    @Autowired
    private WebTestClient webTestClient;

    private UnitList unitList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitList createEntity() {
        UnitList unitList = new UnitList()
            .createTimeStamp(DEFAULT_CREATE_TIME_STAMP)
            .num(DEFAULT_NUM)
            .displayName(DEFAULT_DISPLAY_NAME)
            .type(DEFAULT_TYPE)
            .presenter(DEFAULT_PRESENTER);
        return unitList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitList createUpdatedEntity() {
        UnitList unitList = new UnitList()
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .num(UPDATED_NUM)
            .displayName(UPDATED_DISPLAY_NAME)
            .type(UPDATED_TYPE)
            .presenter(UPDATED_PRESENTER);
        return unitList;
    }

    @BeforeEach
    public void initTest() {
        unitListRepository.deleteAll().block();
        unitList = createEntity();
    }

    @Test
    void createUnitList() throws Exception {
        int databaseSizeBeforeCreate = unitListRepository.findAll().collectList().block().size();
        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeCreate + 1);
        UnitList testUnitList = unitListList.get(unitListList.size() - 1);
        assertThat(testUnitList.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testUnitList.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testUnitList.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testUnitList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUnitList.getPresenter()).isEqualTo(DEFAULT_PRESENTER);
    }

    @Test
    void createUnitListWithExistingId() throws Exception {
        // Create the UnitList with an existing ID
        unitList.setId("existing_id");
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        int databaseSizeBeforeCreate = unitListRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUnitListsAsStream() {
        // Initialize the database
        unitListRepository.save(unitList).block();

        List<UnitList> unitListList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UnitListDTO.class)
            .getResponseBody()
            .map(unitListMapper::toEntity)
            .filter(unitList::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(unitListList).isNotNull();
        assertThat(unitListList).hasSize(1);
        UnitList testUnitList = unitListList.get(0);
        assertThat(testUnitList.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testUnitList.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testUnitList.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testUnitList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUnitList.getPresenter()).isEqualTo(DEFAULT_PRESENTER);
    }

    @Test
    void getAllUnitLists() {
        // Initialize the database
        unitListRepository.save(unitList).block();

        // Get all the unitListList
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
            .value(hasItem(unitList.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].num")
            .value(hasItem(DEFAULT_NUM))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].presenter")
            .value(hasItem(DEFAULT_PRESENTER.toString()));
    }

    @Test
    void getUnitList() {
        // Initialize the database
        unitListRepository.save(unitList).block();

        // Get the unitList
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, unitList.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(unitList.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.num")
            .value(is(DEFAULT_NUM))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.presenter")
            .value(is(DEFAULT_PRESENTER.toString()));
    }

    @Test
    void getNonExistingUnitList() {
        // Get the unitList
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUnitList() throws Exception {
        // Initialize the database
        unitListRepository.save(unitList).block();

        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();

        // Update the unitList
        UnitList updatedUnitList = unitListRepository.findById(unitList.getId()).block();
        updatedUnitList
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .num(UPDATED_NUM)
            .displayName(UPDATED_DISPLAY_NAME)
            .type(UPDATED_TYPE)
            .presenter(UPDATED_PRESENTER);
        UnitListDTO unitListDTO = unitListMapper.toDto(updatedUnitList);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, unitListDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
        UnitList testUnitList = unitListList.get(unitListList.size() - 1);
        assertThat(testUnitList.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testUnitList.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testUnitList.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUnitList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUnitList.getPresenter()).isEqualTo(UPDATED_PRESENTER);
    }

    @Test
    void putNonExistingUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, unitListDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUnitListWithPatch() throws Exception {
        // Initialize the database
        unitListRepository.save(unitList).block();

        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();

        // Update the unitList using partial update
        UnitList partialUpdatedUnitList = new UnitList();
        partialUpdatedUnitList.setId(unitList.getId());

        partialUpdatedUnitList.displayName(UPDATED_DISPLAY_NAME).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUnitList.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitList))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
        UnitList testUnitList = unitListList.get(unitListList.size() - 1);
        assertThat(testUnitList.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testUnitList.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testUnitList.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUnitList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUnitList.getPresenter()).isEqualTo(DEFAULT_PRESENTER);
    }

    @Test
    void fullUpdateUnitListWithPatch() throws Exception {
        // Initialize the database
        unitListRepository.save(unitList).block();

        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();

        // Update the unitList using partial update
        UnitList partialUpdatedUnitList = new UnitList();
        partialUpdatedUnitList.setId(unitList.getId());

        partialUpdatedUnitList
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .num(UPDATED_NUM)
            .displayName(UPDATED_DISPLAY_NAME)
            .type(UPDATED_TYPE)
            .presenter(UPDATED_PRESENTER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUnitList.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitList))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
        UnitList testUnitList = unitListList.get(unitListList.size() - 1);
        assertThat(testUnitList.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testUnitList.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testUnitList.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUnitList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUnitList.getPresenter()).isEqualTo(UPDATED_PRESENTER);
    }

    @Test
    void patchNonExistingUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, unitListDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUnitList() throws Exception {
        int databaseSizeBeforeUpdate = unitListRepository.findAll().collectList().block().size();
        unitList.setId(UUID.randomUUID().toString());

        // Create the UnitList
        UnitListDTO unitListDTO = unitListMapper.toDto(unitList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(unitListDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UnitList in the database
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUnitList() {
        // Initialize the database
        unitListRepository.save(unitList).block();

        int databaseSizeBeforeDelete = unitListRepository.findAll().collectList().block().size();

        // Delete the unitList
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, unitList.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UnitList> unitListList = unitListRepository.findAll().collectList().block();
        assertThat(unitListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
