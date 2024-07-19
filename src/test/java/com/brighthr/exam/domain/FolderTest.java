package com.brighthr.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brighthr.exam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Folder.class);
        Folder folder1 = new Folder();
        folder1.setId(1L);
        Folder folder2 = new Folder();
        folder2.setId(folder1.getId());
        assertThat(folder1).isEqualTo(folder2);
        folder2.setId(2L);
        assertThat(folder1).isNotEqualTo(folder2);
        folder1.setId(null);
        assertThat(folder1).isNotEqualTo(folder2);
    }
}
