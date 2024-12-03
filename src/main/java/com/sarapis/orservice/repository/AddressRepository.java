package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    @Query("SELECT new Attribute(id, linkId, linkType, linkEntity, value, taxonomyTerm, label) FROM Attribute WHERE linkId = ?1")
    List<Attribute> getAttributes(String organizationId);

    @Query("SELECT new Metadata(id, resourceId, resourceType, lastActionDate, lastActionType, fieldName, previousValue, replacementValue, updatedBy) FROM Metadata WHERE resourceId = ?1")
    List<Metadata> getMetadata(String organizationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attribute WHERE linkId = ?1")
    void deleteAttributes(String organizationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Metadata WHERE resourceId = ?1")
    void deleteMetadata(String organizationId);
}
