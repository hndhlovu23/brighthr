package com.brighthr.exam.service.dto;

import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.domain.Folder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.brighthr.exam.domain.File} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDTO implements Serializable {

    private Long id;

    private Long filetypeId;

    private Long folderId;

    @NotNull
    @Size(min = 2, max = 160)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    private String fileUrl;

    @NotNull
    private Date added;

    private Long size;

    private String typeName;

    private String foldername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiletypeId() {
        return filetypeId;
    }

    public void setFiletypeId(Long filetypeId) {
        this.filetypeId = filetypeId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }

        FileDTO fileDTO = (FileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "FileDTO{" +
            "id=" +
            id +
            ", filetypeId=" +
            filetypeId +
            ", folderId=" +
            folderId +
            ", name='" +
            name +
            '\'' +
            ", fileUrl='" +
            fileUrl +
            '\'' +
            ", added=" +
            added +
            ", size=" +
            size +
            ", typeName='" +
            typeName +
            '\'' +
            ", foldername='" +
            foldername +
            '\'' +
            '}'
        );
    }
}
