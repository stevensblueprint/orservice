package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
  List<Address> findByLocationId(String locationId);
}
