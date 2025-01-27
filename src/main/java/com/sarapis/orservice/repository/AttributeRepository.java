package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Attribute;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String> {
    @Query("SELECT new Attribute(id, linkId, linkType, linkEntity, value, label, taxonomyTerm) FROM Attribute WHERE linkId = ?1")
    List<Attribute> getRelatedAttributes(String linkId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attribute WHERE linkId = ?1")
    void deleteRelatedAttributes(String linkId);
}
