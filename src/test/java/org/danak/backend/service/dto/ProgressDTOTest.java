package org.danak.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.danak.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgressDTO.class);
        ProgressDTO progressDTO1 = new ProgressDTO();
        progressDTO1.setId("id1");
        ProgressDTO progressDTO2 = new ProgressDTO();
        assertThat(progressDTO1).isNotEqualTo(progressDTO2);
        progressDTO2.setId(progressDTO1.getId());
        assertThat(progressDTO1).isEqualTo(progressDTO2);
        progressDTO2.setId("id2");
        assertThat(progressDTO1).isNotEqualTo(progressDTO2);
        progressDTO1.setId(null);
        assertThat(progressDTO1).isNotEqualTo(progressDTO2);
    }
}
