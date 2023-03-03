package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.UnitConfig;
import org.danak.backend.repository.UnitConfigRepository;
import org.danak.backend.service.UnitConfigService;
import org.danak.backend.service.dto.UnitConfigDTO;
import org.danak.backend.service.mapper.UnitConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UnitConfig}.
 */
@Service
public class UnitConfigServiceImpl implements UnitConfigService {

    private final Logger log = LoggerFactory.getLogger(UnitConfigServiceImpl.class);

    private final UnitConfigRepository unitConfigRepository;

    private final UnitConfigMapper unitConfigMapper;

    public UnitConfigServiceImpl(UnitConfigRepository unitConfigRepository, UnitConfigMapper unitConfigMapper) {
        this.unitConfigRepository = unitConfigRepository;
        this.unitConfigMapper = unitConfigMapper;
    }

    @Override
    public Mono<UnitConfigDTO> save(UnitConfigDTO unitConfigDTO) {
        log.debug("Request to save UnitConfig : {}", unitConfigDTO);
        return unitConfigRepository.save(unitConfigMapper.toEntity(unitConfigDTO)).map(unitConfigMapper::toDto);
    }

    @Override
    public Mono<UnitConfigDTO> update(UnitConfigDTO unitConfigDTO) {
        log.debug("Request to save UnitConfig : {}", unitConfigDTO);
        return unitConfigRepository.save(unitConfigMapper.toEntity(unitConfigDTO)).map(unitConfigMapper::toDto);
    }

    @Override
    public Mono<UnitConfigDTO> partialUpdate(UnitConfigDTO unitConfigDTO) {
        log.debug("Request to partially update UnitConfig : {}", unitConfigDTO);

        return unitConfigRepository
            .findById(unitConfigDTO.getId())
            .map(existingUnitConfig -> {
                unitConfigMapper.partialUpdate(existingUnitConfig, unitConfigDTO);

                return existingUnitConfig;
            })
            .flatMap(unitConfigRepository::save)
            .map(unitConfigMapper::toDto);
    }

    @Override
    public Flux<UnitConfigDTO> findAll() {
        log.debug("Request to get all UnitConfigs");
        return unitConfigRepository.findAll().map(unitConfigMapper::toDto);
    }

    public Mono<Long> countAll() {
        return unitConfigRepository.count();
    }

    @Override
    public Mono<UnitConfigDTO> findOne(String id) {
        log.debug("Request to get UnitConfig : {}", id);
        return unitConfigRepository.findById(id).map(unitConfigMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete UnitConfig : {}", id);
        return unitConfigRepository.deleteById(id);
    }
}
