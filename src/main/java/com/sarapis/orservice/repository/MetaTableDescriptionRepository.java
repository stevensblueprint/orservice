package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.MetaTableDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaTableDescriptionRepository extends JpaRepository<MetaTableDescription, String> {
}
