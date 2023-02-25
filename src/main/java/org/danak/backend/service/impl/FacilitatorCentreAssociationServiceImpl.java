package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.FacilitatorCentreAssociation;
import org.danak.backend.repository.FacilitatorCentreAssociationRepository;
import org.danak.backend.service.FacilitatorCentreAssociationService;
import org.danak.backend.service.dto.FacilitatorCentreAssociationDTO;
import org.danak.backend.service.mapper.FacilitatorCentreAssociationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link FacilitatorCentreAssociation}.
 */
@Service
public class FacilitatorCentreAssociationServiceImpl implements FacilitatorCentreAssociationService {

    private final Logger log = LoggerFactory.getLogger(FacilitatorCentreAssociationServiceImpl.class);

    private final FacilitatorCentreAssociationRepository facilitatorCentreAssociationRepository;

    private final FacilitatorCentreAssociationMapper facilitatorCentreAssociationMapper;

    public FacilitatorCentreAssociationServiceImpl(
        FacilitatorCentreAssociationRepository facilitatorCentreAssociationRepository,
        FacilitatorCentreAssociationMapper facilitatorCentreAssociationMapper
    ) {
        this.facilitatorCentreAssociationRepository = facilitatorCentreAssociationRepository;
        this.facilitatorCentreAssociationMapper = facilitatorCentreAssociationMapper;
    }

    @Override
    public Mono<FacilitatorCentreAssociationDTO> save(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO) {
        log.debug("Request to save FacilitatorCentreAssociation : {}", facilitatorCentreAssociationDTO);
        return facilitatorCentreAssociationRepository
            .save(facilitatorCentreAssociationMapper.toEntity(facilitatorCentreAssociationDTO))
            .map(facilitatorCentreAssociationMapper::toDto);
    }

    @Override
    public Mono<FacilitatorCentreAssociationDTO> update(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO) {
        log.debug("Request to save FacilitatorCentreAssociation : {}", facilitatorCentreAssociationDTO);
        return facilitatorCentreAssociationRepository
            .save(facilitatorCentreAssociationMapper.toEntity(facilitatorCentreAssociationDTO))
            .map(facilitatorCentreAssociationMapper::toDto);
    }

    @Override
    public Mono<FacilitatorCentreAssociationDTO> partialUpdate(FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO) {
        log.debug("Request to partially update FacilitatorCentreAssociation : {}", facilitatorCentreAssociationDTO);

        return facilitatorCentreAssociationRepository
            .findById(facilitatorCentreAssociationDTO.getId())
            .map(existingFacilitatorCentreAssociation -> {
                facilitatorCentreAssociationMapper.partialUpdate(existingFacilitatorCentreAssociation, facilitatorCentreAssociationDTO);

                return existingFacilitatorCentreAssociation;
            })
            .flatMap(facilitatorCentreAssociationRepository::save)
            .map(facilitatorCentreAssociationMapper::toDto);
    }

    @Override
    public Flux<FacilitatorCentreAssociationDTO> findAll() {
        log.debug("Request to get all FacilitatorCentreAssociations");
        return facilitatorCentreAssociationRepository.findAll().map(facilitatorCentreAssociationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return facilitatorCentreAssociationRepository.count();
    }

    @Override
    public Mono<FacilitatorCentreAssociationDTO> findOne(String id) {
        log.debug("Request to get FacilitatorCentreAssociation : {}", id);
        return facilitatorCentreAssociationRepository.findById(id).map(facilitatorCentreAssociationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete FacilitatorCentreAssociation : {}", id);
        return facilitatorCentreAssociationRepository.deleteById(id);
    }
}
