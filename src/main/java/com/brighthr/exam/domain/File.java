package com.brighthr.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "name", length = 160, nullable = false)
    private String name;

    @NotNull
    @Column(name = "added", nullable = false)
    private Date added;

    @NotNull
    @Size(min = 2, max = 160)
    @Column(name = "file_url", length = 160, nullable = false)
    private String fileUrl;

    @NotNull
    @Column(name = "size")
    private Long size;

    @ManyToOne
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Folder folder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Filetype filetype;

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Filetype getFiletype() {
        return filetype;
    }

    public void setFiletype(Filetype filetype) {
        this.filetype = filetype;
    }

    @Override
    public String toString() {
        return (
            "File{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", added=" +
            added +
            ", fileUrl='" +
            fileUrl +
            '\'' +
            ", size=" +
            size +
            ", folder=" +
            folder +
            ", filetype=" +
            filetype +
            '}'
        );
    }
}
