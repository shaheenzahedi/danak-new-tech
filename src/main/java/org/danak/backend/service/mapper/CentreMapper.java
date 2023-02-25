package org.danak.backend.service.mapper;

import org.danak.backend.domain.Centre;
import org.danak.backend.domain.City;
import org.danak.backend.service.dto.CentreDTO;
import org.danak.backend.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Centre} and its DTO {@link CentreDTO}.
 */
@Mapper(componentModel = "spring")
public interface CentreMapper extends EntityMapper<CentreDTO, Centre> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    CentreDTO toDto(Centre s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);
}
