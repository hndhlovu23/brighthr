package com.brighthr.exam.service.impl;

import com.brighthr.exam.domain.Folder;
import com.brighthr.exam.repository.FolderRepository;
import com.brighthr.exam.service.FolderService;
import com.brighthr.exam.service.dto.FolderDTO;
import com.brighthr.exam.service.mapper.FolderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Folder}.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {

    private final Logger log = LoggerFactory.getLogger(FolderServiceImpl.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    public FolderServiceImpl(FolderRepository folderRepository, FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    @Override
    public FolderDTO save(FolderDTO folderDTO) {
        log.debug("Request to save Folder : {}", folderDTO);
        Folder folder = folderMapper.toEntity(folderDTO);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public FolderDTO update(FolderDTO folderDTO) {
        log.debug("Request to update Folder : {}", folderDTO);
        Folder folder = folderMapper.toEntity(folderDTO);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public Optional<FolderDTO> partialUpdate(FolderDTO folderDTO) {
        log.debug("Request to partially update Folder : {}", folderDTO);

        return folderRepository
            .findById(folderDTO.getId())
            .map(existingFolder -> {
                folderMapper.partialUpdate(existingFolder, folderDTO);

                return existingFolder;
            })
            .map(folderRepository::save)
            .map(folderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FolderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Folders");
        return folderRepository.findAll(pageable).map(folderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FolderDTO> findOne(Long id) {
        log.debug("Request to get Folder : {}", id);
        return folderRepository.findById(id).map(folderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Folder : {}", id);
        folderRepository.deleteById(id);
    }
}
