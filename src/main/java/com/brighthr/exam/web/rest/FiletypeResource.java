package com.brighthr.exam.web.rest;

import com.brighthr.exam.repository.FiletypeRepository;
import com.brighthr.exam.service.FiletypeService;
import com.brighthr.exam.service.dto.FiletypeDTO;
import com.brighthr.exam.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.brighthr.exam.domain.Filetype}.
 */
@RestController
@RequestMapping("/api")
public class FiletypeResource {

    private final Logger log = LoggerFactory.getLogger(FiletypeResource.class);

    private static final String ENTITY_NAME = "filetype";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiletypeService filetypeService;

    private final FiletypeRepository filetypeRepository;

    public FiletypeResource(FiletypeService filetypeService, FiletypeRepository filetypeRepository) {
        this.filetypeService = filetypeService;
        this.filetypeRepository = filetypeRepository;
    }

    /**
     * {@code POST  /filetypes} : Create a new filetype.
     *
     * @param filetypeDTO the filetypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filetypeDTO, or with status {@code 400 (Bad Request)} if the filetype has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filetypes")
    public ResponseEntity<FiletypeDTO> createFiletype(@Valid @RequestBody FiletypeDTO filetypeDTO) throws URISyntaxException {
        log.debug("REST request to save Filetype : {}", filetypeDTO);
        if (filetypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new filetype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiletypeDTO result = filetypeService.save(filetypeDTO);
        return ResponseEntity
            .created(new URI("/api/filetypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filetypes/:id} : Updates an existing filetype.
     *
     * @param id the id of the filetypeDTO to save.
     * @param filetypeDTO the filetypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filetypeDTO,
     * or with status {@code 400 (Bad Request)} if the filetypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filetypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filetypes/{id}")
    public ResponseEntity<FiletypeDTO> updateFiletype(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiletypeDTO filetypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Filetype : {}, {}", id, filetypeDTO);
        if (filetypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filetypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiletypeDTO result = filetypeService.update(filetypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filetypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filetypes/:id} : Partial updates given fields of an existing filetype, field will ignore if it is null
     *
     * @param id the id of the filetypeDTO to save.
     * @param filetypeDTO the filetypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filetypeDTO,
     * or with status {@code 400 (Bad Request)} if the filetypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filetypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filetypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filetypes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiletypeDTO> partialUpdateFiletype(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiletypeDTO filetypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Filetype partially : {}, {}", id, filetypeDTO);
        if (filetypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filetypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiletypeDTO> result = filetypeService.partialUpdate(filetypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filetypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filetypes} : get all the filetypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filetypes in body.
     */
    @GetMapping("/filetypes")
    public ResponseEntity<List<FiletypeDTO>> getAllFiletypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Filetypes");
        Page<FiletypeDTO> page = filetypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filetypes/:id} : get the "id" filetype.
     *
     * @param id the id of the filetypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filetypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filetypes/{id}")
    public ResponseEntity<FiletypeDTO> getFiletype(@PathVariable Long id) {
        log.debug("REST request to get Filetype : {}", id);
        Optional<FiletypeDTO> filetypeDTO = filetypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filetypeDTO);
    }

    /**
     * {@code DELETE  /filetypes/:id} : delete the "id" filetype.
     *
     * @param id the id of the filetypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filetypes/{id}")
    public ResponseEntity<Void> deleteFiletype(@PathVariable Long id) {
        log.debug("REST request to delete Filetype : {}", id);
        filetypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
