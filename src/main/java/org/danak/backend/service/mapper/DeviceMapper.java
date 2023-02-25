package org.danak.backend.service.mapper;

import org.danak.backend.domain.City;
import org.danak.backend.domain.Device;
import org.danak.backend.service.dto.CityDTO;
import org.danak.backend.service.dto.DeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    DeviceDTO toDto(Device s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);
}
