package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacilitatorCentreAssociationMapperTest {

    private FacilitatorCentreAssociationMapper facilitatorCentreAssociationMapper;

    @BeforeEach
    public void setUp() {
        facilitatorCentreAssociationMapper = new FacilitatorCentreAssociationMapperImpl();
    }
}
