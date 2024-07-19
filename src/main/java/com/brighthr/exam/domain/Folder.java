package com.brighthr.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Folder.
 */
@Entity
@Table(name = "folder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "name", length = 160, nullable = false)
    private String name;

    @OneToMany(mappedBy = "folder")
    @JsonIgnoreProperties(value = { "folder", "filetype" }, allowSetters = true)
    private Set<File> files = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Folder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Folder name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<File> getFiles() {
        return this.files;
    }

    public void setFiles(Set<File> files) {
        if (this.files != null) {
            this.files.forEach(i -> i.setFolder(null));
        }
        if (files != null) {
            files.forEach(i -> i.setFolder(this));
        }
        this.files = files;
    }

    public Folder files(Set<File> files) {
        this.setFiles(files);
        return this;
    }

    public Folder addFile(File file) {
        this.files.add(file);
        file.setFolder(this);
        return this;
    }

    public Folder removeFile(File file) {
        this.files.remove(file);
        file.setFolder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Folder)) {
            return false;
        }
        return id != null && id.equals(((Folder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Folder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
