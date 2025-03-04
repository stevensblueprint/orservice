package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
  List<Schedule> findByLocationId(String locationId);
}
