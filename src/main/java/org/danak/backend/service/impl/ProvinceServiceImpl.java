package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Province;
import org.danak.backend.repository.ProvinceRepository;
import org.danak.backend.service.ProvinceService;
import org.danak.backend.service.dto.ProvinceDTO;
import org.danak.backend.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    @Override
    public Mono<ProvinceDTO> save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        return provinceRepository.save(provinceMapper.toEntity(provinceDTO)).map(provinceMapper::toDto);
    }

    @Override
    public Mono<ProvinceDTO> update(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        return provinceRepository.save(provinceMapper.toEntity(provinceDTO)).map(provinceMapper::toDto);
    }

    @Override
    public Mono<ProvinceDTO> partialUpdate(ProvinceDTO provinceDTO) {
        log.debug("Request to partially update Province : {}", provinceDTO);

        return provinceRepository
            .findById(provinceDTO.getId())
            .map(existingProvince -> {
                provinceMapper.partialUpdate(existingProvince, provinceDTO);

                return existingProvince;
            })
            .flatMap(provinceRepository::save)
            .map(provinceMapper::toDto);
    }

    @Override
    public Flux<ProvinceDTO> findAll() {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll().map(provinceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return provinceRepository.count();
    }

    @Override
    public Mono<ProvinceDTO> findOne(String id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id).map(provinceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Province : {}", id);
        return provinceRepository.deleteById(id);
    }
}
