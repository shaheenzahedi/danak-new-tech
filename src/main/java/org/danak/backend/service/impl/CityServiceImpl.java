package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.City;
import org.danak.backend.repository.CityRepository;
import org.danak.backend.service.CityService;
import org.danak.backend.service.dto.CityDTO;
import org.danak.backend.service.mapper.CityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link City}.
 */
@Service
public class CityServiceImpl implements CityService {

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public Mono<CityDTO> save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        return cityRepository.save(cityMapper.toEntity(cityDTO)).map(cityMapper::toDto);
    }

    @Override
    public Mono<CityDTO> update(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        return cityRepository.save(cityMapper.toEntity(cityDTO)).map(cityMapper::toDto);
    }

    @Override
    public Mono<CityDTO> partialUpdate(CityDTO cityDTO) {
        log.debug("Request to partially update City : {}", cityDTO);

        return cityRepository
            .findById(cityDTO.getId())
            .map(existingCity -> {
                cityMapper.partialUpdate(existingCity, cityDTO);

                return existingCity;
            })
            .flatMap(cityRepository::save)
            .map(cityMapper::toDto);
    }

    @Override
    public Flux<CityDTO> findAll() {
        log.debug("Request to get all Cities");
        return cityRepository.findAll().map(cityMapper::toDto);
    }

    public Mono<Long> countAll() {
        return cityRepository.count();
    }

    @Override
    public Mono<CityDTO> findOne(String id) {
        log.debug("Request to get City : {}", id);
        return cityRepository.findById(id).map(cityMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete City : {}", id);
        return cityRepository.deleteById(id);
    }
}
