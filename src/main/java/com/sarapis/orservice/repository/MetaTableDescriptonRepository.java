package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.MetaTableDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaTableDescriptonRepository extends JpaRepository<MetaTableDescription, String> {
}
