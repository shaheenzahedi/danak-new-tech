package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitConfig.class);
        UnitConfig unitConfig1 = new UnitConfig();
        unitConfig1.setId("id1");
        UnitConfig unitConfig2 = new UnitConfig();
        unitConfig2.setId(unitConfig1.getId());
        assertThat(unitConfig1).isEqualTo(unitConfig2);
        unitConfig2.setId("id2");
        assertThat(unitConfig1).isNotEqualTo(unitConfig2);
        unitConfig1.setId(null);
        assertThat(unitConfig1).isNotEqualTo(unitConfig2);
    }
}
