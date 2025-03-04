package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, String>,
    JpaSpecificationExecutor<Taxonomy> {
}
