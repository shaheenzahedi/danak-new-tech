package org.danak.backend.service.mapper;

import org.danak.backend.domain.Centre;
import org.danak.backend.domain.Facilitator;
import org.danak.backend.domain.FacilitatorCentreAssociation;
import org.danak.backend.service.dto.CentreDTO;
import org.danak.backend.service.dto.FacilitatorCentreAssociationDTO;
import org.danak.backend.service.dto.FacilitatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacilitatorCentreAssociation} and its DTO {@link FacilitatorCentreAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacilitatorCentreAssociationMapper extends EntityMapper<FacilitatorCentreAssociationDTO, FacilitatorCentreAssociation> {
    @Mapping(target = "facilitator", source = "facilitator", qualifiedByName = "facilitatorId")
    @Mapping(target = "centre", source = "centre", qualifiedByName = "centreId")
    FacilitatorCentreAssociationDTO toDto(FacilitatorCentreAssociation s);

    @Named("facilitatorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacilitatorDTO toDtoFacilitatorId(Facilitator facilitator);

    @Named("centreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentreDTO toDtoCentreId(Centre centre);
}
