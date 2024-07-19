package com.brighthr.exam.repository;

import com.brighthr.exam.domain.Folder;
import com.brighthr.exam.domain.Folder;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Folder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("SELECT folder FROM Folder folder where folder.name= :name")
    List<Folder> findAllByFolderName(@Param("name") String name);

    @Query("SELECT folder FROM Folder folder where folder.name = :name")
    Folder findByFolderName(@Param("name") String name);

    @Query("SELECT folder FROM Folder folder")
    List<Folder> findAllFolders();
}
