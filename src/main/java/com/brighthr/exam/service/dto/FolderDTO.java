package com.brighthr.exam.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.brighthr.exam.domain.Folder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FolderDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 160)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FolderDTO)) {
            return false;
        }

        FolderDTO folderDTO = (FolderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, folderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FolderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
