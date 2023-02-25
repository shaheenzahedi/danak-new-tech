package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Child.class);
        Child child1 = new Child();
        child1.setId("id1");
        Child child2 = new Child();
        child2.setId(child1.getId());
        assertThat(child1).isEqualTo(child2);
        child2.setId("id2");
        assertThat(child1).isNotEqualTo(child2);
        child1.setId(null);
        assertThat(child1).isNotEqualTo(child2);
    }
}
