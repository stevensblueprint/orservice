package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Taxonomy;
import org.springframework.data.jpa.domain.Specification;

public class TaxonomySpecifications {
  public static Specification<Taxonomy> hasSearchTerm(String term) {
    return (root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("name"), likeTerm),
          criteriaBuilder.like(root.get("description"), likeTerm));
    };
  }
}
