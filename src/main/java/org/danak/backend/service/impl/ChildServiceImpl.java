package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Child;
import org.danak.backend.repository.ChildRepository;
import org.danak.backend.service.ChildService;
import org.danak.backend.service.dto.ChildDTO;
import org.danak.backend.service.mapper.ChildMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Child}.
 */
@Service
public class ChildServiceImpl implements ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildServiceImpl.class);

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    public ChildServiceImpl(ChildRepository childRepository, ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
    }

    @Override
    public Mono<ChildDTO> save(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        return childRepository.save(childMapper.toEntity(childDTO)).map(childMapper::toDto);
    }

    @Override
    public Mono<ChildDTO> update(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        return childRepository.save(childMapper.toEntity(childDTO)).map(childMapper::toDto);
    }

    @Override
    public Mono<ChildDTO> partialUpdate(ChildDTO childDTO) {
        log.debug("Request to partially update Child : {}", childDTO);

        return childRepository
            .findById(childDTO.getId())
            .map(existingChild -> {
                childMapper.partialUpdate(existingChild, childDTO);

                return existingChild;
            })
            .flatMap(childRepository::save)
            .map(childMapper::toDto);
    }

    @Override
    public Flux<ChildDTO> findAll() {
        log.debug("Request to get all Children");
        return childRepository.findAll().map(childMapper::toDto);
    }

    public Mono<Long> countAll() {
        return childRepository.count();
    }

    @Override
    public Mono<ChildDTO> findOne(String id) {
        log.debug("Request to get Child : {}", id);
        return childRepository.findById(id).map(childMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Child : {}", id);
        return childRepository.deleteById(id);
    }
}
