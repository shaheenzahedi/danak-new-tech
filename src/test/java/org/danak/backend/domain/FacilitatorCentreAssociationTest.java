package org.danak.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilitatorCentreAssociationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilitatorCentreAssociation.class);
        FacilitatorCentreAssociation facilitatorCentreAssociation1 = new FacilitatorCentreAssociation();
        facilitatorCentreAssociation1.setId("id1");
        FacilitatorCentreAssociation facilitatorCentreAssociation2 = new FacilitatorCentreAssociation();
        facilitatorCentreAssociation2.setId(facilitatorCentreAssociation1.getId());
        assertThat(facilitatorCentreAssociation1).isEqualTo(facilitatorCentreAssociation2);
        facilitatorCentreAssociation2.setId("id2");
        assertThat(facilitatorCentreAssociation1).isNotEqualTo(facilitatorCentreAssociation2);
        facilitatorCentreAssociation1.setId(null);
        assertThat(facilitatorCentreAssociation1).isNotEqualTo(facilitatorCentreAssociation2);
    }
}
