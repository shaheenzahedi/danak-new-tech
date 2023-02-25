package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Device;
import org.danak.backend.repository.DeviceRepository;
import org.danak.backend.service.DeviceService;
import org.danak.backend.service.dto.DeviceDTO;
import org.danak.backend.service.mapper.DeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public Mono<DeviceDTO> save(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        return deviceRepository.save(deviceMapper.toEntity(deviceDTO)).map(deviceMapper::toDto);
    }

    @Override
    public Mono<DeviceDTO> update(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        return deviceRepository.save(deviceMapper.toEntity(deviceDTO)).map(deviceMapper::toDto);
    }

    @Override
    public Mono<DeviceDTO> partialUpdate(DeviceDTO deviceDTO) {
        log.debug("Request to partially update Device : {}", deviceDTO);

        return deviceRepository
            .findById(deviceDTO.getId())
            .map(existingDevice -> {
                deviceMapper.partialUpdate(existingDevice, deviceDTO);

                return existingDevice;
            })
            .flatMap(deviceRepository::save)
            .map(deviceMapper::toDto);
    }

    @Override
    public Flux<DeviceDTO> findAll() {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll().map(deviceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return deviceRepository.count();
    }

    @Override
    public Mono<DeviceDTO> findOne(String id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Device : {}", id);
        return deviceRepository.deleteById(id);
    }
}
