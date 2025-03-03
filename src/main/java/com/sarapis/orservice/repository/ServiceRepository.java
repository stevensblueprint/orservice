package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceRepository extends JpaRepository<Service, String>,
    JpaSpecificationExecutor<Service> {
}
