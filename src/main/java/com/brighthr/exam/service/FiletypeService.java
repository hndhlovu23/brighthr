package com.brighthr.exam.service;

import com.brighthr.exam.service.dto.FiletypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.brighthr.exam.domain.Filetype}.
 */
public interface FiletypeService {
    /**
     * Save a filetype.
     *
     * @param filetypeDTO the entity to save.
     * @return the persisted entity.
     */
    FiletypeDTO save(FiletypeDTO filetypeDTO);

    /**
     * Updates a filetype.
     *
     * @param filetypeDTO the entity to update.
     * @return the persisted entity.
     */
    FiletypeDTO update(FiletypeDTO filetypeDTO);

    /**
     * Partially updates a filetype.
     *
     * @param filetypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiletypeDTO> partialUpdate(FiletypeDTO filetypeDTO);

    /**
     * Get all the filetypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiletypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" filetype.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiletypeDTO> findOne(Long id);

    /**
     * Delete the "id" filetype.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
