package com.brighthr.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Folder Entity and relationships.
 */
@Entity
@Table(name = "filetype")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Filetype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "typeName", length = 160, nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "filetype")
    @JsonIgnoreProperties(value = { "folder", "filetype" }, allowSetters = true)
    private Set<File> files = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Filetype id(Long id) {
        this.setId(id);
        return this;
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

    public Set<File> getFiles() {
        return this.files;
    }

    public void setFiles(Set<File> files) {
        if (this.files != null) {
            this.files.forEach(i -> i.setFiletype(null));
        }
        if (files != null) {
            files.forEach(i -> i.setFiletype(this));
        }
        this.files = files;
    }

    public Filetype files(Set<File> files) {
        this.setFiles(files);
        return this;
    }

    public Filetype addFile(File file) {
        this.files.add(file);
        file.setFiletype(this);
        return this;
    }

    public Filetype removeFile(File file) {
        this.files.remove(file);
        file.setFiletype(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filetype)) {
            return false;
        }
        return id != null && id.equals(((Filetype) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Filetype{" + "id=" + id + ", typeName='" + typeName + '\'' + '}';
    }
}
