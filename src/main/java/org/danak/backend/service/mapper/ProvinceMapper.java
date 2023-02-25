package org.danak.backend.service.mapper;

import org.danak.backend.domain.Country;
import org.danak.backend.domain.Province;
import org.danak.backend.service.dto.CountryDTO;
import org.danak.backend.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    ProvinceDTO toDto(Province s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
