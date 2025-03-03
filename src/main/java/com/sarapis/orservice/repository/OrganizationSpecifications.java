package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Organization;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecifications {
  public static Specification<Organization> hasSearchTerm(String term) {
    return ((root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("name"), likeTerm),
          criteriaBuilder.like(root.get("description"), likeTerm)
      );
    });
  }
  
}
