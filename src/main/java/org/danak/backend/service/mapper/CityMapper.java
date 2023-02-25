package org.danak.backend.service.mapper;

import org.danak.backend.domain.City;
import org.danak.backend.domain.Province;
import org.danak.backend.service.dto.CityDTO;
import org.danak.backend.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    CityDTO toDto(City s);

    @Named("provinceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoProvinceId(Province province);
}
