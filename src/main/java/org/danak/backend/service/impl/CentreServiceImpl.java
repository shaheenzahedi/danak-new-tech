package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Centre;
import org.danak.backend.repository.CentreRepository;
import org.danak.backend.service.CentreService;
import org.danak.backend.service.dto.CentreDTO;
import org.danak.backend.service.mapper.CentreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Centre}.
 */
@Service
public class CentreServiceImpl implements CentreService {

    private final Logger log = LoggerFactory.getLogger(CentreServiceImpl.class);

    private final CentreRepository centreRepository;

    private final CentreMapper centreMapper;

    public CentreServiceImpl(CentreRepository centreRepository, CentreMapper centreMapper) {
        this.centreRepository = centreRepository;
        this.centreMapper = centreMapper;
    }

    @Override
    public Mono<CentreDTO> save(CentreDTO centreDTO) {
        log.debug("Request to save Centre : {}", centreDTO);
        return centreRepository.save(centreMapper.toEntity(centreDTO)).map(centreMapper::toDto);
    }

    @Override
    public Mono<CentreDTO> update(CentreDTO centreDTO) {
        log.debug("Request to save Centre : {}", centreDTO);
        return centreRepository.save(centreMapper.toEntity(centreDTO)).map(centreMapper::toDto);
    }

    @Override
    public Mono<CentreDTO> partialUpdate(CentreDTO centreDTO) {
        log.debug("Request to partially update Centre : {}", centreDTO);

        return centreRepository
            .findById(centreDTO.getId())
            .map(existingCentre -> {
                centreMapper.partialUpdate(existingCentre, centreDTO);

                return existingCentre;
            })
            .flatMap(centreRepository::save)
            .map(centreMapper::toDto);
    }

    @Override
    public Flux<CentreDTO> findAll() {
        log.debug("Request to get all Centres");
        return centreRepository.findAll().map(centreMapper::toDto);
    }

    public Mono<Long> countAll() {
        return centreRepository.count();
    }

    @Override
    public Mono<CentreDTO> findOne(String id) {
        log.debug("Request to get Centre : {}", id);
        return centreRepository.findById(id).map(centreMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Centre : {}", id);
        return centreRepository.deleteById(id);
    }
}
