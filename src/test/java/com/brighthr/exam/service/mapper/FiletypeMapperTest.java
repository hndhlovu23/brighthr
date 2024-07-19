package com.brighthr.exam.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiletypeMapperTest {

    private FiletypeMapper filetypeMapper;

    @BeforeEach
    public void setUp() {
        filetypeMapper = new FiletypeMapperImpl();
    }
}
