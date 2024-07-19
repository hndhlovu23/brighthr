package com.brighthr.exam.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.brighthr.exam.domain.Filetype} entity.
 */
@Schema(description = "Folder Entity and relationships.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiletypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 160)
    private String typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiletypeDTO)) {
            return false;
        }

        FiletypeDTO filetypeDTO = (FiletypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filetypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "FiletypeDTO{" + "id=" + id + ", typeName='" + typeName + '\'' + '}';
    }
}
