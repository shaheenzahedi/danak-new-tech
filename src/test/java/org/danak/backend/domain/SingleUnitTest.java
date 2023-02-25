package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleUnit.class);
        SingleUnit singleUnit1 = new SingleUnit();
        singleUnit1.setId("id1");
        SingleUnit singleUnit2 = new SingleUnit();
        singleUnit2.setId(singleUnit1.getId());
        assertThat(singleUnit1).isEqualTo(singleUnit2);
        singleUnit2.setId("id2");
        assertThat(singleUnit1).isNotEqualTo(singleUnit2);
        singleUnit1.setId(null);
        assertThat(singleUnit1).isNotEqualTo(singleUnit2);
    }
}
