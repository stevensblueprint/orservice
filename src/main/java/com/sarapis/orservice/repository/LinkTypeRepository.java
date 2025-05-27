package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkTypeRepository extends JpaRepository<LinkType, String> {
}
