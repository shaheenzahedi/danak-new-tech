package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleUnitMapperTest {

    private SingleUnitMapper singleUnitMapper;

    @BeforeEach
    public void setUp() {
        singleUnitMapper = new SingleUnitMapperImpl();
    }
}
