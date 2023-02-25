package org.danak.backend.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.danak.backend.domain.Country;
import org.danak.backend.repository.CountryRepository;
import org.danak.backend.service.CountryService;
import org.danak.backend.service.dto.CountryDTO;
import org.danak.backend.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Country}.
 */
@Service
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public Mono<CountryDTO> save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        return countryRepository.save(countryMapper.toEntity(countryDTO)).map(countryMapper::toDto);
    }

    @Override
    public Mono<CountryDTO> update(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        return countryRepository.save(countryMapper.toEntity(countryDTO)).map(countryMapper::toDto);
    }

    @Override
    public Mono<CountryDTO> partialUpdate(CountryDTO countryDTO) {
        log.debug("Request to partially update Country : {}", countryDTO);

        return countryRepository
            .findById(countryDTO.getId())
            .map(existingCountry -> {
                countryMapper.partialUpdate(existingCountry, countryDTO);

                return existingCountry;
            })
            .flatMap(countryRepository::save)
            .map(countryMapper::toDto);
    }

    @Override
    public Flux<CountryDTO> findAll() {
        log.debug("Request to get all Countries");
        return countryRepository.findAll().map(countryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return countryRepository.count();
    }

    @Override
    public Mono<CountryDTO> findOne(String id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id).map(countryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Country : {}", id);
        return countryRepository.deleteById(id);
    }
}
