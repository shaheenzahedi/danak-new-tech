package org.danak.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacilitatorMapperTest {

    private FacilitatorMapper facilitatorMapper;

    @BeforeEach
    public void setUp() {
        facilitatorMapper = new FacilitatorMapperImpl();
    }
}
