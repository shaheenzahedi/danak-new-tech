package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitList.class);
        UnitList unitList1 = new UnitList();
        unitList1.setId("id1");
        UnitList unitList2 = new UnitList();
        unitList2.setId(unitList1.getId());
        assertThat(unitList1).isEqualTo(unitList2);
        unitList2.setId("id2");
        assertThat(unitList1).isNotEqualTo(unitList2);
        unitList1.setId(null);
        assertThat(unitList1).isNotEqualTo(unitList2);
    }
}
