package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChildMapperTest {

    private ChildMapper childMapper;

    @BeforeEach
    public void setUp() {
        childMapper = new ChildMapperImpl();
    }
}
