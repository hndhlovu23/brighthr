package com.brighthr.exam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brighthr.exam.IntegrationTest;
import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.repository.FiletypeRepository;
import com.brighthr.exam.service.dto.FiletypeDTO;
import com.brighthr.exam.service.mapper.FiletypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FiletypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiletypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filetypes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiletypeRepository filetypeRepository;

    @Autowired
    private FiletypeMapper filetypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiletypeMockMvc;

    private Filetype filetype;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filetype createEntity(EntityManager em) {
        Filetype filetype = new Filetype();
        filetype.setTypeName(DEFAULT_TYPE);
        return filetype;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filetype createUpdatedEntity(EntityManager em) {
        Filetype filetype = new Filetype();
        filetype.setTypeName(UPDATED_TYPE);
        return filetype;
    }

    @BeforeEach
    public void initTest() {
        filetype = createEntity(em);
    }

    @Test
    @Transactional
    void createFiletype() throws Exception {
        int databaseSizeBeforeCreate = filetypeRepository.findAll().size();
        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);
        restFiletypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filetypeDTO)))
            .andExpect(status().isCreated());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeCreate + 1);
        Filetype testFiletype = filetypeList.get(filetypeList.size() - 1);
        assertThat(testFiletype.getTypeName()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createFiletypeWithExistingId() throws Exception {
        // Create the Filetype with an existing ID
        filetype.setId(1L);
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        int databaseSizeBeforeCreate = filetypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiletypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filetypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = filetypeRepository.findAll().size();
        // set the field null
        filetype.setTypeName(null);

        // Create the Filetype, which fails.
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        restFiletypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filetypeDTO)))
            .andExpect(status().isBadRequest());

        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiletypes() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        // Get all the filetypeList
        restFiletypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filetype.getId().intValue())))
            .andExpect(jsonPath("$.[*].setTypeName").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getFiletype() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        // Get the filetype
        restFiletypeMockMvc
            .perform(get(ENTITY_API_URL_ID, filetype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filetype.getId().intValue()))
            .andExpect(jsonPath("$.setTypeName").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingFiletype() throws Exception {
        // Get the filetype
        restFiletypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiletype() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();

        // Update the filetype
        Filetype updatedFiletype = filetypeRepository.findById(filetype.getId()).get();
        // Disconnect from session so that the updates on updatedFiletype are not directly saved in db
        em.detach(updatedFiletype);
        updatedFiletype.setTypeName(UPDATED_TYPE);
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(updatedFiletype);

        restFiletypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filetypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
        Filetype testFiletype = filetypeList.get(filetypeList.size() - 1);
        assertThat(testFiletype.getTypeName()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filetypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filetypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiletypeWithPatch() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();

        // Update the filetype using partial update
        Filetype partialUpdatedFiletype = new Filetype();
        partialUpdatedFiletype.setId(filetype.getId());

        partialUpdatedFiletype.setTypeName(UPDATED_TYPE);

        restFiletypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiletype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiletype))
            )
            .andExpect(status().isOk());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
        Filetype testFiletype = filetypeList.get(filetypeList.size() - 1);
        assertThat(testFiletype.getTypeName()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFiletypeWithPatch() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();

        // Update the filetype using partial update
        Filetype partialUpdatedFiletype = new Filetype();
        partialUpdatedFiletype.setId(filetype.getId());

        partialUpdatedFiletype.setTypeName(UPDATED_TYPE);

        restFiletypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiletype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiletype))
            )
            .andExpect(status().isOk());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
        Filetype testFiletype = filetypeList.get(filetypeList.size() - 1);
        assertThat(testFiletype.getTypeName()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filetypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiletype() throws Exception {
        int databaseSizeBeforeUpdate = filetypeRepository.findAll().size();
        filetype.setId(count.incrementAndGet());

        // Create the Filetype
        FiletypeDTO filetypeDTO = filetypeMapper.toDto(filetype);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiletypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filetypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filetype in the database
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiletype() throws Exception {
        // Initialize the database
        filetypeRepository.saveAndFlush(filetype);

        int databaseSizeBeforeDelete = filetypeRepository.findAll().size();

        // Delete the filetype
        restFiletypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, filetype.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filetype> filetypeList = filetypeRepository.findAll();
        assertThat(filetypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
