package com.brighthr.exam.service.mapper;

import com.brighthr.exam.domain.File;
import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.domain.Folder;
import com.brighthr.exam.service.dto.FileDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { FiletypeMapper.class, FolderMapper.class })
public interface FileMapper extends EntityMapper<FileDTO, File> {
    @Mapping(target = "folderId", source = "folder.id")
    @Mapping(target = "foldername", source = "folder.name")
    @Mapping(target = "filetypeId", source = "filetype.id")
    @Mapping(target = "typeName", source = "filetype.typeName")
    FileDTO toDto(File file);

    @Mapping(target = "folder", source = "folderId", qualifiedByName = "folderFromId")
    @Mapping(target = "filetype", source = "filetypeId", qualifiedByName = "filetypeFromId")
    File toEntity(FileDTO fileDTO);

    @Named("folderFromId")
    default Folder folderFromId(Long id) {
        if (id == null) {
            return null;
        }
        Folder folder = new Folder();
        folder.setId(id);
        return folder;
    }

    @Named("filetypeFromId")
    default Filetype filetypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Filetype filetype = new Filetype();
        filetype.setId(id);
        return filetype;
    }
}
