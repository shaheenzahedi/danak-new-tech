package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitListMapperTest {

    private UnitListMapper unitListMapper;

    @BeforeEach
    public void setUp() {
        unitListMapper = new UnitListMapperImpl();
    }
}
