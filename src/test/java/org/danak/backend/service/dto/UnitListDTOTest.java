package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitListDTO.class);
        UnitListDTO unitListDTO1 = new UnitListDTO();
        unitListDTO1.setId("id1");
        UnitListDTO unitListDTO2 = new UnitListDTO();
        assertThat(unitListDTO1).isNotEqualTo(unitListDTO2);
        unitListDTO2.setId(unitListDTO1.getId());
        assertThat(unitListDTO1).isEqualTo(unitListDTO2);
        unitListDTO2.setId("id2");
        assertThat(unitListDTO1).isNotEqualTo(unitListDTO2);
        unitListDTO1.setId(null);
        assertThat(unitListDTO1).isNotEqualTo(unitListDTO2);
    }
}
