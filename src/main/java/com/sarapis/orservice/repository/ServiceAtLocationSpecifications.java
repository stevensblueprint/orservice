package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.ServiceAtLocation;
import org.springframework.data.jpa.domain.Specification;

public class ServiceAtLocationSpecifications {
  public static Specification<ServiceAtLocation> hasSearchTerm(String term) {
    return (root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("description"), likeTerm));
    };
  }
}
