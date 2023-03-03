package org.danak.backend.service.mapper;

import org.danak.backend.domain.Facilitator;
import org.danak.backend.domain.User;
import org.danak.backend.service.dto.FacilitatorDTO;
import org.danak.backend.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facilitator} and its DTO {@link FacilitatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacilitatorMapper extends EntityMapper<FacilitatorDTO, Facilitator> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "referedBy", source = "referedBy", qualifiedByName = "userId")
    FacilitatorDTO toDto(Facilitator s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
