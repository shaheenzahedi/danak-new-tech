package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitConfigMapperTest {

    private UnitConfigMapper unitConfigMapper;

    @BeforeEach
    public void setUp() {
        unitConfigMapper = new UnitConfigMapperImpl();
    }
}
