package org.danak.backend.service.mapper;

import org.danak.backend.domain.Child;
import org.danak.backend.domain.Progress;
import org.danak.backend.domain.SingleUnit;
import org.danak.backend.service.dto.ChildDTO;
import org.danak.backend.service.dto.ProgressDTO;
import org.danak.backend.service.dto.SingleUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Progress} and its DTO {@link ProgressDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgressMapper extends EntityMapper<ProgressDTO, Progress> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    @Mapping(target = "singleUnit", source = "singleUnit", qualifiedByName = "singleUnitId")
    ProgressDTO toDto(Progress s);

    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);

    @Named("singleUnitId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SingleUnitDTO toDtoSingleUnitId(SingleUnit singleUnit);
}
