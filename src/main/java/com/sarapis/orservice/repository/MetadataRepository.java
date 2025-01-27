package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Metadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, String> {
    @Query("SELECT new Metadata(id, resourceId, resourceType, lastActionDate, lastActionType, fieldName, previousValue, replacementValue, updatedBy) FROM Metadata WHERE resourceId = ?1")
    List<Metadata> getRelatedMetadata(String resourceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Metadata WHERE resourceId = ?1")
    void deleteRelatedMetadata(String resourceId);
}
