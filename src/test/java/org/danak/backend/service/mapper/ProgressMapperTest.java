package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgressMapperTest {

    private ProgressMapper progressMapper;

    @BeforeEach
    public void setUp() {
        progressMapper = new ProgressMapperImpl();
    }
}
