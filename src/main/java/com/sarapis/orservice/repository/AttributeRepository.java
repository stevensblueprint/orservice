package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Attribute;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String> {
  List<Attribute> findByLinkId(String linkId);

  List<Attribute> findByLinkIdAndLinkEntity(String linkId, String linkEntity);

  void deleteByLinkIdAndLinkEntity(String linkId, String linkEntity);
}
