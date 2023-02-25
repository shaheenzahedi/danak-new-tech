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
import org.danak.backend.domain.Device;
import org.danak.backend.repository.DeviceRepository;
import org.danak.backend.service.dto.DeviceDTO;
import org.danak.backend.service.mapper.DeviceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeviceResourceIT {

    private static final Instant DEFAULT_CREATE_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_UNIVERSAL_ID = UUID.randomUUID();
    private static final UUID UPDATED_UNIVERSAL_ID = UUID.randomUUID();

    private static final String DEFAULT_GLOBAL_NUM = "AAAAAAAAAA";
    private static final String UPDATED_GLOBAL_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR_BUILT = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_BUILT = "BBBBBBBBBB";

    private static final String DEFAULT_ANDROID_ID = "AAAAAAAAAA";
    private static final String UPDATED_ANDROID_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity() {
        Device device = new Device()
            .createTimeStamp(DEFAULT_CREATE_TIME_STAMP)
            .universalId(DEFAULT_UNIVERSAL_ID)
            .globalNum(DEFAULT_GLOBAL_NUM)
            .model(DEFAULT_MODEL)
            .yearBuilt(DEFAULT_YEAR_BUILT)
            .androidId(DEFAULT_ANDROID_ID);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity() {
        Device device = new Device()
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .universalId(UPDATED_UNIVERSAL_ID)
            .globalNum(UPDATED_GLOBAL_NUM)
            .model(UPDATED_MODEL)
            .yearBuilt(UPDATED_YEAR_BUILT)
            .androidId(UPDATED_ANDROID_ID);
        return device;
    }

    @BeforeEach
    public void initTest() {
        deviceRepository.deleteAll().block();
        device = createEntity();
    }

    @Test
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().collectList().block().size();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testDevice.getUniversalId()).isEqualTo(DEFAULT_UNIVERSAL_ID);
        assertThat(testDevice.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testDevice.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDevice.getYearBuilt()).isEqualTo(DEFAULT_YEAR_BUILT);
        assertThat(testDevice.getAndroidId()).isEqualTo(DEFAULT_ANDROID_ID);
    }

    @Test
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId("existing_id");
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        int databaseSizeBeforeCreate = deviceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDevicesAsStream() {
        // Initialize the database
        deviceRepository.save(device).block();

        List<Device> deviceList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DeviceDTO.class)
            .getResponseBody()
            .map(deviceMapper::toEntity)
            .filter(device::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(deviceList).isNotNull();
        assertThat(deviceList).hasSize(1);
        Device testDevice = deviceList.get(0);
        assertThat(testDevice.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testDevice.getUniversalId()).isEqualTo(DEFAULT_UNIVERSAL_ID);
        assertThat(testDevice.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testDevice.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDevice.getYearBuilt()).isEqualTo(DEFAULT_YEAR_BUILT);
        assertThat(testDevice.getAndroidId()).isEqualTo(DEFAULT_ANDROID_ID);
    }

    @Test
    void getAllDevices() {
        // Initialize the database
        deviceRepository.save(device).block();

        // Get all the deviceList
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
            .value(hasItem(device.getId()))
            .jsonPath("$.[*].createTimeStamp")
            .value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.[*].universalId")
            .value(hasItem(DEFAULT_UNIVERSAL_ID.toString()))
            .jsonPath("$.[*].globalNum")
            .value(hasItem(DEFAULT_GLOBAL_NUM))
            .jsonPath("$.[*].model")
            .value(hasItem(DEFAULT_MODEL))
            .jsonPath("$.[*].yearBuilt")
            .value(hasItem(DEFAULT_YEAR_BUILT))
            .jsonPath("$.[*].androidId")
            .value(hasItem(DEFAULT_ANDROID_ID));
    }

    @Test
    void getDevice() {
        // Initialize the database
        deviceRepository.save(device).block();

        // Get the device
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, device.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(device.getId()))
            .jsonPath("$.createTimeStamp")
            .value(is(DEFAULT_CREATE_TIME_STAMP.toString()))
            .jsonPath("$.universalId")
            .value(is(DEFAULT_UNIVERSAL_ID.toString()))
            .jsonPath("$.globalNum")
            .value(is(DEFAULT_GLOBAL_NUM))
            .jsonPath("$.model")
            .value(is(DEFAULT_MODEL))
            .jsonPath("$.yearBuilt")
            .value(is(DEFAULT_YEAR_BUILT))
            .jsonPath("$.androidId")
            .value(is(DEFAULT_ANDROID_ID));
    }

    @Test
    void getNonExistingDevice() {
        // Get the device
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDevice() throws Exception {
        // Initialize the database
        deviceRepository.save(device).block();

        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).block();
        updatedDevice
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .universalId(UPDATED_UNIVERSAL_ID)
            .globalNum(UPDATED_GLOBAL_NUM)
            .model(UPDATED_MODEL)
            .yearBuilt(UPDATED_YEAR_BUILT)
            .androidId(UPDATED_ANDROID_ID);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deviceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testDevice.getUniversalId()).isEqualTo(UPDATED_UNIVERSAL_ID);
        assertThat(testDevice.getGlobalNum()).isEqualTo(UPDATED_GLOBAL_NUM);
        assertThat(testDevice.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDevice.getYearBuilt()).isEqualTo(UPDATED_YEAR_BUILT);
        assertThat(testDevice.getAndroidId()).isEqualTo(UPDATED_ANDROID_ID);
    }

    @Test
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deviceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.save(device).block();

        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice.model(UPDATED_MODEL).yearBuilt(UPDATED_YEAR_BUILT).androidId(UPDATED_ANDROID_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
        assertThat(testDevice.getUniversalId()).isEqualTo(DEFAULT_UNIVERSAL_ID);
        assertThat(testDevice.getGlobalNum()).isEqualTo(DEFAULT_GLOBAL_NUM);
        assertThat(testDevice.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDevice.getYearBuilt()).isEqualTo(UPDATED_YEAR_BUILT);
        assertThat(testDevice.getAndroidId()).isEqualTo(UPDATED_ANDROID_ID);
    }

    @Test
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.save(device).block();

        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .createTimeStamp(UPDATED_CREATE_TIME_STAMP)
            .universalId(UPDATED_UNIVERSAL_ID)
            .globalNum(UPDATED_GLOBAL_NUM)
            .model(UPDATED_MODEL)
            .yearBuilt(UPDATED_YEAR_BUILT)
            .androidId(UPDATED_ANDROID_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
        assertThat(testDevice.getUniversalId()).isEqualTo(UPDATED_UNIVERSAL_ID);
        assertThat(testDevice.getGlobalNum()).isEqualTo(UPDATED_GLOBAL_NUM);
        assertThat(testDevice.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testDevice.getYearBuilt()).isEqualTo(UPDATED_YEAR_BUILT);
        assertThat(testDevice.getAndroidId()).isEqualTo(UPDATED_ANDROID_ID);
    }

    @Test
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deviceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().collectList().block().size();
        device.setId(UUID.randomUUID().toString());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deviceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDevice() {
        // Initialize the database
        deviceRepository.save(device).block();

        int databaseSizeBeforeDelete = deviceRepository.findAll().collectList().block().size();

        // Delete the device
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, device.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll().collectList().block();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
