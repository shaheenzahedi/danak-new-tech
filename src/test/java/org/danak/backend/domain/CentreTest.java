package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Centre.class);
        Centre centre1 = new Centre();
        centre1.setId("id1");
        Centre centre2 = new Centre();
        centre2.setId(centre1.getId());
        assertThat(centre1).isEqualTo(centre2);
        centre2.setId("id2");
        assertThat(centre1).isNotEqualTo(centre2);
        centre1.setId(null);
        assertThat(centre1).isNotEqualTo(centre2);
    }
}
