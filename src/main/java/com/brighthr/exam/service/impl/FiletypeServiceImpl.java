package com.brighthr.exam.service.impl;

import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.repository.FiletypeRepository;
import com.brighthr.exam.service.FiletypeService;
import com.brighthr.exam.service.dto.FiletypeDTO;
import com.brighthr.exam.service.mapper.FiletypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filetype}.
 */
@Service
@Transactional
public class FiletypeServiceImpl implements FiletypeService {

    private final Logger log = LoggerFactory.getLogger(FiletypeServiceImpl.class);

    private final FiletypeRepository filetypeRepository;

    private final FiletypeMapper filetypeMapper;

    public FiletypeServiceImpl(FiletypeRepository filetypeRepository, FiletypeMapper filetypeMapper) {
        this.filetypeRepository = filetypeRepository;
        this.filetypeMapper = filetypeMapper;
    }

    @Override
    public FiletypeDTO save(FiletypeDTO filetypeDTO) {
        log.debug("Request to save Filetype : {}", filetypeDTO);
        Filetype filetype = filetypeMapper.toEntity(filetypeDTO);
        filetype = filetypeRepository.save(filetype);
        return filetypeMapper.toDto(filetype);
    }

    @Override
    public FiletypeDTO update(FiletypeDTO filetypeDTO) {
        log.debug("Request to update Filetype : {}", filetypeDTO);
        Filetype filetype = filetypeMapper.toEntity(filetypeDTO);
        filetype = filetypeRepository.save(filetype);
        return filetypeMapper.toDto(filetype);
    }

    @Override
    public Optional<FiletypeDTO> partialUpdate(FiletypeDTO filetypeDTO) {
        log.debug("Request to partially update Filetype : {}", filetypeDTO);

        return filetypeRepository
            .findById(filetypeDTO.getId())
            .map(existingFiletype -> {
                filetypeMapper.partialUpdate(existingFiletype, filetypeDTO);

                return existingFiletype;
            })
            .map(filetypeRepository::save)
            .map(filetypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiletypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Filetypes");
        return filetypeRepository.findAll(pageable).map(filetypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiletypeDTO> findOne(Long id) {
        log.debug("Request to get Filetype : {}", id);
        return filetypeRepository.findById(id).map(filetypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filetype : {}", id);
        filetypeRepository.deleteById(id);
    }
}
