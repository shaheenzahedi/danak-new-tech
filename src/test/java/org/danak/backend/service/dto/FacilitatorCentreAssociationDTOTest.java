package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilitatorCentreAssociationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilitatorCentreAssociationDTO.class);
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO1 = new FacilitatorCentreAssociationDTO();
        facilitatorCentreAssociationDTO1.setId("id1");
        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO2 = new FacilitatorCentreAssociationDTO();
        assertThat(facilitatorCentreAssociationDTO1).isNotEqualTo(facilitatorCentreAssociationDTO2);
        facilitatorCentreAssociationDTO2.setId(facilitatorCentreAssociationDTO1.getId());
        assertThat(facilitatorCentreAssociationDTO1).isEqualTo(facilitatorCentreAssociationDTO2);
        facilitatorCentreAssociationDTO2.setId("id2");
        assertThat(facilitatorCentreAssociationDTO1).isNotEqualTo(facilitatorCentreAssociationDTO2);
        facilitatorCentreAssociationDTO1.setId(null);
        assertThat(facilitatorCentreAssociationDTO1).isNotEqualTo(facilitatorCentreAssociationDTO2);
    }
}
