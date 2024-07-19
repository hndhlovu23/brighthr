package com.brighthr.exam.service.mapper;

import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.service.dto.FiletypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filetype} and its DTO {@link FiletypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FiletypeMapper extends EntityMapper<FiletypeDTO, Filetype> {}
