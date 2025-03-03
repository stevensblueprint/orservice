package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, String> {

}
