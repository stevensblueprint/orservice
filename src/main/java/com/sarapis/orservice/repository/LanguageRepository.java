package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Language;
import com.sarapis.orservice.entity.Metadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    @Query("SELECT new Attribute(id, linkId, linkType, linkEntity, value, taxonomyTerm, label) FROM Attribute WHERE linkId = ?1")
    List<Attribute> getAttributes(String languageId);

    @Query("SELECT new Metadata(id, resourceId, resourceType, lastActionDate, lastActionType, fieldName, previousValue, replacementValue, updatedBy) FROM Metadata WHERE resourceId = ?1")
    List<Metadata> getMetadata(String languageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attribute WHERE linkId = ?1")
    void deleteAttributes(String languageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Metadata WHERE resourceId = ?1")
    void deleteMetadata(String languageId);
}
