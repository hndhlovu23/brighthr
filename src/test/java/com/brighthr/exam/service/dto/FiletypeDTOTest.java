package com.brighthr.exam.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.brighthr.exam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiletypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiletypeDTO.class);
        FiletypeDTO filetypeDTO1 = new FiletypeDTO();
        filetypeDTO1.setId(1L);
        FiletypeDTO filetypeDTO2 = new FiletypeDTO();
        assertThat(filetypeDTO1).isNotEqualTo(filetypeDTO2);
        filetypeDTO2.setId(filetypeDTO1.getId());
        assertThat(filetypeDTO1).isEqualTo(filetypeDTO2);
        filetypeDTO2.setId(2L);
        assertThat(filetypeDTO1).isNotEqualTo(filetypeDTO2);
        filetypeDTO1.setId(null);
        assertThat(filetypeDTO1).isNotEqualTo(filetypeDTO2);
    }
}
