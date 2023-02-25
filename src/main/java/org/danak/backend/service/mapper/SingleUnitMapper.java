package org.danak.backend.service.mapper;

import org.danak.backend.domain.SingleUnit;
import org.danak.backend.domain.UnitList;
import org.danak.backend.service.dto.SingleUnitDTO;
import org.danak.backend.service.dto.UnitListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleUnit} and its DTO {@link SingleUnitDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleUnitMapper extends EntityMapper<SingleUnitDTO, SingleUnit> {
    @Mapping(target = "unitList", source = "unitList", qualifiedByName = "unitListId")
    SingleUnitDTO toDto(SingleUnit s);

    @Named("unitListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UnitListDTO toDtoUnitListId(UnitList unitList);
}
