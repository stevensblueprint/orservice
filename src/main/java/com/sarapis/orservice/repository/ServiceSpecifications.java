package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Service;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class ServiceSpecifications {
  public static Specification<Service> hasSearchTerm(String term) {
    return (root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("name"), likeTerm),
          criteriaBuilder.like(root.get("description"), likeTerm)
      );
    };
  }

  public static Specification<Service> modifiedAfter(LocalDate date) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThan(root.get("lastModified"), date);
  }
}
