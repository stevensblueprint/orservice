package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.MetadataDTO.Response;
import com.sarapis.orservice.mapper.MetadataMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sarapis.orservice.utils.MetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
  private final MetadataRepository metadataRepository;
  private final MetadataMapper metadataMapper;

  private final OrganizationRepository organizationRepository;
  private final UrlRepository urlRepository;
  private final ProgramRepository programRepository;
  private final PhoneRepository phoneRepository;
  private final OrganizationIdentifiersRepository organizationIdentifiersRepository;
  private final FundingRepository fundingRepository;
  private final ContactRepository contactRepository;
  private final LanguageRepository languageRepository;
  private final AccessibilityRepository accessibilityRepository;
  private final AddressRepository addressRepository;
  private final ScheduleRepository scheduleRepository;
  private final LocationRepository locationRepository;
  private final ServiceAreaRepository serviceAreaRepository;
  private final CostOptionRepository costOptionRepository;
  private final ServiceRepository serviceRepository;
  private final RequiredDocumentRepository requiredDocumentRepository;
  private final ServiceAtLocationRepository serviceAtLocationRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Response> getMetadataByResourceIdAndResourceType(String resourceId, String resourceType) {
    List<Metadata> metadataList = metadataRepository.findByResourceIdAndResourceType(resourceId, resourceType);
    return metadataList.stream().map(metadataMapper::toResponseDTO).collect(Collectors.toList());
  }


  @Override
  @Transactional
  public MetadataDTO.Response createMetadata(MetadataDTO.Request requestDto) {
    Metadata metadata = metadataMapper.toEntity(requestDto);
    Metadata savedMetadata = metadataRepository.save(metadata);
    return metadataMapper.toResponseDTO(savedMetadata);
  }

  private JpaRepository<?, String> getRepository(String resourceType) {
    return switch(resourceType) {
      case MetadataUtils.ORGANIZATION_RESOURCE_TYPE -> this.organizationRepository;
      case MetadataUtils.URL_RESOURCE_TYPE -> this.urlRepository;
      case MetadataUtils.PROGRAM_RESOURCE_TYPE -> this.programRepository;
      case MetadataUtils.PHONE_RESOURCE_TYPE -> this.phoneRepository;
      case MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE -> this.organizationIdentifiersRepository;
      case MetadataUtils.FUNDING_RESOURCE_TYPE -> this.fundingRepository;
      case MetadataUtils.CONTACT_RESOURCE_TYPE -> this.contactRepository;
      case MetadataUtils.LANGUAGE_RESOURCE_TYPE -> this.languageRepository;
      case MetadataUtils.ACCESSIBILITY_RESOURCE_TYPE -> this.accessibilityRepository;
      case MetadataUtils.ADDRESS_RESOURCE_TYPE -> this.addressRepository;
      case MetadataUtils.SCHEDULE_RESOURCE_TYPE -> this.scheduleRepository;
      case MetadataUtils.LOCATION_RESOURCE_TYPE -> this.locationRepository;
      case MetadataUtils.SERVICE_AREA_RESOURCE_TYPE -> this.serviceAreaRepository;
      case MetadataUtils.COST_OPTION_RESOURCE_TYPE -> this.costOptionRepository;
      case MetadataUtils.SERVICE_RESOURCE_TYPE -> this.serviceRepository;
      case MetadataUtils.REQUIRED_DOCUMENT_RESOURCE_TYPE -> this.requiredDocumentRepository;
      case MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE -> this.serviceAtLocationRepository;
      default -> throw new RuntimeException("");
    };
  }
}
