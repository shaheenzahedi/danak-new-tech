package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Province.class);
        Province province1 = new Province();
        province1.setId("id1");
        Province province2 = new Province();
        province2.setId(province1.getId());
        assertThat(province1).isEqualTo(province2);
        province2.setId("id2");
        assertThat(province1).isNotEqualTo(province2);
        province1.setId(null);
        assertThat(province1).isNotEqualTo(province2);
    }
}
