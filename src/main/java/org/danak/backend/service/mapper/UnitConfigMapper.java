package org.danak.backend.service.mapper;

import org.danak.backend.domain.UnitConfig;
import org.danak.backend.service.dto.UnitConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UnitConfig} and its DTO {@link UnitConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitConfigMapper extends EntityMapper<UnitConfigDTO, UnitConfig> {}
