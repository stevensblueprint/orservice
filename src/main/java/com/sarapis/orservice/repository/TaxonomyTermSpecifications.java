package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.TaxonomyTerm;
import jakarta.persistence.criteria.Subquery;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class TaxonomyTermSpecifications {
  public static Specification<TaxonomyTerm> hasSearchTerm(String term) {
    return (root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("name"), likeTerm),
          criteriaBuilder.like(root.get("description"), likeTerm)
      );
    };
  }

  public static Specification<TaxonomyTerm> hasTaxonomyId(String taxonomyId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("taxonomyDetail").get("id"), taxonomyId);
  }

  public static Specification<TaxonomyTerm> hasParentId(String parentId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("parent").get("id"), parentId);
  }

  public static Specification<TaxonomyTerm> isLeaf() {
    return (root, query, cb) -> {
      Subquery<Long> sub = Objects.requireNonNull(query).subquery(Long.class);
      var subRoot = sub.from(TaxonomyTerm.class);
      sub.select(cb.count(subRoot));
      sub.where(cb.equal(subRoot.get("parent"), root));
      return cb.equal(sub, 0L);
    };
  }
}
