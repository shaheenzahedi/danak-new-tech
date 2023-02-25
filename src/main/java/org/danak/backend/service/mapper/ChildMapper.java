package org.danak.backend.service.mapper;

import org.danak.backend.domain.Centre;
import org.danak.backend.domain.Child;
import org.danak.backend.domain.Device;
import org.danak.backend.domain.Facilitator;
import org.danak.backend.domain.User;
import org.danak.backend.service.dto.CentreDTO;
import org.danak.backend.service.dto.ChildDTO;
import org.danak.backend.service.dto.DeviceDTO;
import org.danak.backend.service.dto.FacilitatorDTO;
import org.danak.backend.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "centre", source = "centre", qualifiedByName = "centreId")
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    @Mapping(target = "facilitator", source = "facilitator", qualifiedByName = "facilitatorId")
    ChildDTO toDto(Child s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("centreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentreDTO toDtoCentreId(Centre centre);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);

    @Named("facilitatorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacilitatorDTO toDtoFacilitatorId(Facilitator facilitator);
}
