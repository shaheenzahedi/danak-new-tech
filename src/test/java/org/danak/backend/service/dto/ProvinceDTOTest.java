package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProvinceDTO.class);
        ProvinceDTO provinceDTO1 = new ProvinceDTO();
        provinceDTO1.setId("id1");
        ProvinceDTO provinceDTO2 = new ProvinceDTO();
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO2.setId(provinceDTO1.getId());
        assertThat(provinceDTO1).isEqualTo(provinceDTO2);
        provinceDTO2.setId("id2");
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO1.setId(null);
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
    }
}
