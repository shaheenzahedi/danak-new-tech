package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.UnitList;
import org.danak.backend.repository.UnitListRepository;
import org.danak.backend.service.UnitListService;
import org.danak.backend.service.dto.UnitListDTO;
import org.danak.backend.service.mapper.UnitListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UnitList}.
 */
@Service
public class UnitListServiceImpl implements UnitListService {

    private final Logger log = LoggerFactory.getLogger(UnitListServiceImpl.class);

    private final UnitListRepository unitListRepository;

    private final UnitListMapper unitListMapper;

    public UnitListServiceImpl(UnitListRepository unitListRepository, UnitListMapper unitListMapper) {
        this.unitListRepository = unitListRepository;
        this.unitListMapper = unitListMapper;
    }

    @Override
    public Mono<UnitListDTO> save(UnitListDTO unitListDTO) {
        log.debug("Request to save UnitList : {}", unitListDTO);
        return unitListRepository.save(unitListMapper.toEntity(unitListDTO)).map(unitListMapper::toDto);
    }

    @Override
    public Mono<UnitListDTO> update(UnitListDTO unitListDTO) {
        log.debug("Request to save UnitList : {}", unitListDTO);
        return unitListRepository.save(unitListMapper.toEntity(unitListDTO)).map(unitListMapper::toDto);
    }

    @Override
    public Mono<UnitListDTO> partialUpdate(UnitListDTO unitListDTO) {
        log.debug("Request to partially update UnitList : {}", unitListDTO);

        return unitListRepository
            .findById(unitListDTO.getId())
            .map(existingUnitList -> {
                unitListMapper.partialUpdate(existingUnitList, unitListDTO);

                return existingUnitList;
            })
            .flatMap(unitListRepository::save)
            .map(unitListMapper::toDto);
    }

    @Override
    public Flux<UnitListDTO> findAll() {
        log.debug("Request to get all UnitLists");
        return unitListRepository.findAll().map(unitListMapper::toDto);
    }

    public Mono<Long> countAll() {
        return unitListRepository.count();
    }

    @Override
    public Mono<UnitListDTO> findOne(String id) {
        log.debug("Request to get UnitList : {}", id);
        return unitListRepository.findById(id).map(unitListMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete UnitList : {}", id);
        return unitListRepository.deleteById(id);
    }
}
