package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.SingleUnit;
import org.danak.backend.repository.SingleUnitRepository;
import org.danak.backend.service.SingleUnitService;
import org.danak.backend.service.dto.SingleUnitDTO;
import org.danak.backend.service.mapper.SingleUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link SingleUnit}.
 */
@Service
public class SingleUnitServiceImpl implements SingleUnitService {

    private final Logger log = LoggerFactory.getLogger(SingleUnitServiceImpl.class);

    private final SingleUnitRepository singleUnitRepository;

    private final SingleUnitMapper singleUnitMapper;

    public SingleUnitServiceImpl(SingleUnitRepository singleUnitRepository, SingleUnitMapper singleUnitMapper) {
        this.singleUnitRepository = singleUnitRepository;
        this.singleUnitMapper = singleUnitMapper;
    }

    @Override
    public Mono<SingleUnitDTO> save(SingleUnitDTO singleUnitDTO) {
        log.debug("Request to save SingleUnit : {}", singleUnitDTO);
        return singleUnitRepository.save(singleUnitMapper.toEntity(singleUnitDTO)).map(singleUnitMapper::toDto);
    }

    @Override
    public Mono<SingleUnitDTO> update(SingleUnitDTO singleUnitDTO) {
        log.debug("Request to save SingleUnit : {}", singleUnitDTO);
        return singleUnitRepository.save(singleUnitMapper.toEntity(singleUnitDTO)).map(singleUnitMapper::toDto);
    }

    @Override
    public Mono<SingleUnitDTO> partialUpdate(SingleUnitDTO singleUnitDTO) {
        log.debug("Request to partially update SingleUnit : {}", singleUnitDTO);

        return singleUnitRepository
            .findById(singleUnitDTO.getId())
            .map(existingSingleUnit -> {
                singleUnitMapper.partialUpdate(existingSingleUnit, singleUnitDTO);

                return existingSingleUnit;
            })
            .flatMap(singleUnitRepository::save)
            .map(singleUnitMapper::toDto);
    }

    @Override
    public Flux<SingleUnitDTO> findAll() {
        log.debug("Request to get all SingleUnits");
        return singleUnitRepository.findAll().map(singleUnitMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singleUnitRepository.count();
    }

    @Override
    public Mono<SingleUnitDTO> findOne(String id) {
        log.debug("Request to get SingleUnit : {}", id);
        return singleUnitRepository.findById(id).map(singleUnitMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete SingleUnit : {}", id);
        return singleUnitRepository.deleteById(id);
    }
}
