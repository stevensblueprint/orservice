package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.TaxonomyTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyTermRepository extends JpaRepository<TaxonomyTerm, Long> {
}