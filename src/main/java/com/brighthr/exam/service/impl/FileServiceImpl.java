package com.brighthr.exam.service.impl;

import com.brighthr.exam.domain.File;
import com.brighthr.exam.repository.FileRepository;
import com.brighthr.exam.service.FileService;
import com.brighthr.exam.service.dto.FileDTO;
import com.brighthr.exam.service.mapper.FileMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDTO save(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public FileDTO update(FileDTO fileDTO) {
        log.debug("Request to update File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public Optional<FileDTO> partialUpdate(FileDTO fileDTO) {
        log.debug("Request to partially update File : {}", fileDTO);

        return fileRepository
            .findById(fileDTO.getId())
            .map(existingFile -> {
                fileMapper.partialUpdate(existingFile, fileDTO);

                return existingFile;
            })
            .map(fileRepository::save)
            .map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return fileRepository.findAll(pageable).map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id).map(fileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
    }

    @Override
    public List<FileDTO> findAllByFileName(String name) {
        log.debug("Request to get all  files by file name : {}", name);

        return fileMapper.toDto(fileRepository.findAllByFileName(name));
    }

    @Override
    public List<FileDTO> findAllByFolderName(String name) {
        log.debug("Request to get all  files by folder name : {}", name);

        return fileMapper.toDto(fileRepository.findAllByFolderName(name));
    }

    @Override
    public List<FileDTO> findAllByFolderId(Long id) {
        log.debug("Request to get all files by folder id : {}", id);

        return fileMapper.toDto(fileRepository.findAllByFolderId(id));
    }
}
