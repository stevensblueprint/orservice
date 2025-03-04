package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Phone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {
  List<Phone> findByOrganizationId(String organizationId);
  List<Phone> findByContactId(String contactId);
  List<Phone> findByLocationId(String locationId);
  List<Phone> findByServiceId(String serviceId);
}
