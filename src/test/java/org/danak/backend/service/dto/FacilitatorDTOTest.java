package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilitatorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilitatorDTO.class);
        FacilitatorDTO facilitatorDTO1 = new FacilitatorDTO();
        facilitatorDTO1.setId("id1");
        FacilitatorDTO facilitatorDTO2 = new FacilitatorDTO();
        assertThat(facilitatorDTO1).isNotEqualTo(facilitatorDTO2);
        facilitatorDTO2.setId(facilitatorDTO1.getId());
        assertThat(facilitatorDTO1).isEqualTo(facilitatorDTO2);
        facilitatorDTO2.setId("id2");
        assertThat(facilitatorDTO1).isNotEqualTo(facilitatorDTO2);
        facilitatorDTO1.setId(null);
        assertThat(facilitatorDTO1).isNotEqualTo(facilitatorDTO2);
    }
}
