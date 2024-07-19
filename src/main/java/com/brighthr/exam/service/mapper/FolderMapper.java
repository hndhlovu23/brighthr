package com.brighthr.exam.service.mapper;

import com.brighthr.exam.domain.Folder;
import com.brighthr.exam.service.dto.FolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Folder} and its DTO {@link FolderDTO}.
 */
@Mapper(componentModel = "spring")
public interface FolderMapper extends EntityMapper<FolderDTO, Folder> {}
