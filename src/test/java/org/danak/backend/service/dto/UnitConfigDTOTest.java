package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitConfigDTO.class);
        UnitConfigDTO unitConfigDTO1 = new UnitConfigDTO();
        unitConfigDTO1.setId("id1");
        UnitConfigDTO unitConfigDTO2 = new UnitConfigDTO();
        assertThat(unitConfigDTO1).isNotEqualTo(unitConfigDTO2);
        unitConfigDTO2.setId(unitConfigDTO1.getId());
        assertThat(unitConfigDTO1).isEqualTo(unitConfigDTO2);
        unitConfigDTO2.setId("id2");
        assertThat(unitConfigDTO1).isNotEqualTo(unitConfigDTO2);
        unitConfigDTO1.setId(null);
        assertThat(unitConfigDTO1).isNotEqualTo(unitConfigDTO2);
    }
}
