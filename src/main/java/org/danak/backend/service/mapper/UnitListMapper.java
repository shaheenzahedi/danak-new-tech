package org.danak.backend.service.mapper;

import org.danak.backend.domain.UnitList;
import org.danak.backend.service.dto.UnitListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UnitList} and its DTO {@link UnitListDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitListMapper extends EntityMapper<UnitListDTO, UnitList> {}
