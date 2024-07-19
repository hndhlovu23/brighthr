package com.brighthr.exam.service;

import com.brighthr.exam.domain.File;
import com.brighthr.exam.service.dto.FileDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Service Interface for managing {@link com.brighthr.exam.domain.File}.
 */
public interface FileService {
    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    FileDTO save(FileDTO fileDTO);

    /**
     * Updates a file.
     *
     * @param fileDTO the entity to update.
     * @return the persisted entity.
     */
    FileDTO update(FileDTO fileDTO);

    /**
     * Partially updates a file.
     *
     * @param fileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDTO> partialUpdate(FileDTO fileDTO);

    /**
     * Get all the files.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" file.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDTO> findOne(Long id);

    /**
     * Delete the "id" file.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<FileDTO> findAllByFileName(String name);

    List<FileDTO> findAllByFolderName(String name);

    List<FileDTO> findAllByFolderId(Long id);
}
