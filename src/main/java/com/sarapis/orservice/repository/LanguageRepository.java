package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Language;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
  List<Language> findByPhoneId(String phoneId);
}
