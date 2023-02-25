package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleUnitDTO.class);
        SingleUnitDTO singleUnitDTO1 = new SingleUnitDTO();
        singleUnitDTO1.setId("id1");
        SingleUnitDTO singleUnitDTO2 = new SingleUnitDTO();
        assertThat(singleUnitDTO1).isNotEqualTo(singleUnitDTO2);
        singleUnitDTO2.setId(singleUnitDTO1.getId());
        assertThat(singleUnitDTO1).isEqualTo(singleUnitDTO2);
        singleUnitDTO2.setId("id2");
        assertThat(singleUnitDTO1).isNotEqualTo(singleUnitDTO2);
        singleUnitDTO1.setId(null);
        assertThat(singleUnitDTO1).isNotEqualTo(singleUnitDTO2);
    }
}
