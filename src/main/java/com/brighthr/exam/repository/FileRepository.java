package com.brighthr.exam.repository;

import com.brighthr.exam.domain.File;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT file FROM File file where file.name= :name")
    List<File> findAllByFolderName(@Param("name") String name);

    @Query("SELECT file FROM File file where file.folder.id= :id")
    List<File> findAllByFolderId(@Param("id") Long id);

    @Query("SELECT file FROM File file where file.name = :name")
    File findByFileName(@Param("name") String name);

    @Query("SELECT file FROM File file where file.name= :name")
    List<File> findAllByFileName(@Param("name") String name);

    @Query("SELECT file FROM File file")
    List<File> findAllFiles();
}
