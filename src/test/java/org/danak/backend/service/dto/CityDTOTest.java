package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityDTO.class);
        CityDTO cityDTO1 = new CityDTO();
        cityDTO1.setId("id1");
        CityDTO cityDTO2 = new CityDTO();
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO2.setId(cityDTO1.getId());
        assertThat(cityDTO1).isEqualTo(cityDTO2);
        cityDTO2.setId("id2");
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO1.setId(null);
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
    }
}
