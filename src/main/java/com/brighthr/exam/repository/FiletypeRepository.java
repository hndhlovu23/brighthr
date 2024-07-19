package com.brighthr.exam.repository;

import com.brighthr.exam.domain.Filetype;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Filetypetype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiletypeRepository extends JpaRepository<Filetype, Long> {
    @Query("SELECT filetype FROM Filetype filetype where filetype.typeName= :typeName")
    List<Filetype> findAllByFiletypeName(@Param("typeName") String typeName);

    @Query("SELECT filetype FROM Filetype filetype where filetype.typeName= :typeName")
    Filetype findByFiletypeName(@Param("typeName") String typeName);

    @Query("SELECT filetype FROM Filetype filetype")
    List<Filetype> findAllFiletypes();
}
