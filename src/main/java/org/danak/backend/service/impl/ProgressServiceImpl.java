package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Progress;
import org.danak.backend.repository.ProgressRepository;
import org.danak.backend.service.ProgressService;
import org.danak.backend.service.dto.ProgressDTO;
import org.danak.backend.service.mapper.ProgressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Progress}.
 */
@Service
public class ProgressServiceImpl implements ProgressService {

    private final Logger log = LoggerFactory.getLogger(ProgressServiceImpl.class);

    private final ProgressRepository progressRepository;

    private final ProgressMapper progressMapper;

    public ProgressServiceImpl(ProgressRepository progressRepository, ProgressMapper progressMapper) {
        this.progressRepository = progressRepository;
        this.progressMapper = progressMapper;
    }

    @Override
    public Mono<ProgressDTO> save(ProgressDTO progressDTO) {
        log.debug("Request to save Progress : {}", progressDTO);
        return progressRepository.save(progressMapper.toEntity(progressDTO)).map(progressMapper::toDto);
    }

    @Override
    public Mono<ProgressDTO> update(ProgressDTO progressDTO) {
        log.debug("Request to save Progress : {}", progressDTO);
        return progressRepository.save(progressMapper.toEntity(progressDTO)).map(progressMapper::toDto);
    }

    @Override
    public Mono<ProgressDTO> partialUpdate(ProgressDTO progressDTO) {
        log.debug("Request to partially update Progress : {}", progressDTO);

        return progressRepository
            .findById(progressDTO.getId())
            .map(existingProgress -> {
                progressMapper.partialUpdate(existingProgress, progressDTO);

                return existingProgress;
            })
            .flatMap(progressRepository::save)
            .map(progressMapper::toDto);
    }

    @Override
    public Flux<ProgressDTO> findAll() {
        log.debug("Request to get all Progresses");
        return progressRepository.findAll().map(progressMapper::toDto);
    }

    public Mono<Long> countAll() {
        return progressRepository.count();
    }

    @Override
    public Mono<ProgressDTO> findOne(String id) {
        log.debug("Request to get Progress : {}", id);
        return progressRepository.findById(id).map(progressMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Progress : {}", id);
        return progressRepository.deleteById(id);
    }
}
