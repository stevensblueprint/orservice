package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
  List<Contact> findByOrganizationId(String organizationId);
  List<Contact> findByLocationId(String locationId);
}
