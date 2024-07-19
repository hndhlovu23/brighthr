package com.brighthr.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brighthr.exam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiletypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filetype.class);
        Filetype filetype1 = new Filetype();
        filetype1.setId(1L);
        Filetype filetype2 = new Filetype();
        filetype2.setId(filetype1.getId());
        assertThat(filetype1).isEqualTo(filetype2);
        filetype2.setId(2L);
        assertThat(filetype1).isNotEqualTo(filetype2);
        filetype1.setId(null);
        assertThat(filetype1).isNotEqualTo(filetype2);
    }
}
