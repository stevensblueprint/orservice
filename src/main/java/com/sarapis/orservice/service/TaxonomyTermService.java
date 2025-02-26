package com.sarapis.orservice.service;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxonomyTermService {
  private final TaxonomyTermRepository taxonomyTermRepository;
  private final MetadataRepository metadataRepository;
}
