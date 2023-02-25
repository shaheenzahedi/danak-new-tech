package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreDTO.class);
        CentreDTO centreDTO1 = new CentreDTO();
        centreDTO1.setId("id1");
        CentreDTO centreDTO2 = new CentreDTO();
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
        centreDTO2.setId(centreDTO1.getId());
        assertThat(centreDTO1).isEqualTo(centreDTO2);
        centreDTO2.setId("id2");
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
        centreDTO1.setId(null);
        assertThat(centreDTO1).isNotEqualTo(centreDTO2);
    }
}
