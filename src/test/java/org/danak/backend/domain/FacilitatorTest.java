package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilitatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facilitator.class);
        Facilitator facilitator1 = new Facilitator();
        facilitator1.setId("id1");
        Facilitator facilitator2 = new Facilitator();
        facilitator2.setId(facilitator1.getId());
        assertThat(facilitator1).isEqualTo(facilitator2);
        facilitator2.setId("id2");
        assertThat(facilitator1).isNotEqualTo(facilitator2);
        facilitator1.setId(null);
        assertThat(facilitator1).isNotEqualTo(facilitator2);
    }
}
