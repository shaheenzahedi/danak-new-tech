package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Facilitator;
import org.danak.backend.repository.FacilitatorRepository;
import org.danak.backend.service.FacilitatorService;
import org.danak.backend.service.dto.FacilitatorDTO;
import org.danak.backend.service.mapper.FacilitatorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Facilitator}.
 */
@Service
public class FacilitatorServiceImpl implements FacilitatorService {

    private final Logger log = LoggerFactory.getLogger(FacilitatorServiceImpl.class);

    private final FacilitatorRepository facilitatorRepository;

    private final FacilitatorMapper facilitatorMapper;

    public FacilitatorServiceImpl(FacilitatorRepository facilitatorRepository, FacilitatorMapper facilitatorMapper) {
        this.facilitatorRepository = facilitatorRepository;
        this.facilitatorMapper = facilitatorMapper;
    }

    @Override
    public Mono<FacilitatorDTO> save(FacilitatorDTO facilitatorDTO) {
        log.debug("Request to save Facilitator : {}", facilitatorDTO);
        return facilitatorRepository.save(facilitatorMapper.toEntity(facilitatorDTO)).map(facilitatorMapper::toDto);
    }

    @Override
    public Mono<FacilitatorDTO> update(FacilitatorDTO facilitatorDTO) {
        log.debug("Request to save Facilitator : {}", facilitatorDTO);
        return facilitatorRepository.save(facilitatorMapper.toEntity(facilitatorDTO)).map(facilitatorMapper::toDto);
    }

    @Override
    public Mono<FacilitatorDTO> partialUpdate(FacilitatorDTO facilitatorDTO) {
        log.debug("Request to partially update Facilitator : {}", facilitatorDTO);

        return facilitatorRepository
            .findById(facilitatorDTO.getId())
            .map(existingFacilitator -> {
                facilitatorMapper.partialUpdate(existingFacilitator, facilitatorDTO);

                return existingFacilitator;
            })
            .flatMap(facilitatorRepository::save)
            .map(facilitatorMapper::toDto);
    }

    @Override
    public Flux<FacilitatorDTO> findAll() {
        log.debug("Request to get all Facilitators");
        return facilitatorRepository.findAll().map(facilitatorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return facilitatorRepository.count();
    }

    @Override
    public Mono<FacilitatorDTO> findOne(String id) {
        log.debug("Request to get Facilitator : {}", id);
        return facilitatorRepository.findById(id).map(facilitatorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Facilitator : {}", id);
        return facilitatorRepository.deleteById(id);
    }
}
