package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, String> {
}
