package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, Long> {
}