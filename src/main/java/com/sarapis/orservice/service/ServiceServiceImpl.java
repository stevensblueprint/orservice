package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;
  private final PhoneService phoneService;
  private final ServiceAreaService serviceAreaService;
  private final LanguageService languageService;
  private final FundingService fundingService;
  private final CostOptionService costOptionService;
  private final RequiredDocumentService requiredDocumentService;
  private final ContactService contactService;
  private final MetadataService metadataService;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServices(String search, Integer page, Integer perPage,
      String format, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full) {
    Specification<Service> spec = Specification.where(null);

    if (search != null && !search.isEmpty()) {
      spec = spec.and(ServiceSpecifications.hasSearchTerm(search));
    }

    if (modifiedAfter != null && !modifiedAfter.isEmpty()) {
      LocalDate modifiedDate = LocalDate.parse(modifiedAfter);
      spec = spec.and(ServiceSpecifications.modifiedAfter(modifiedDate));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
    Page<ServiceDTO.Response> dtoPage = servicePage.map(service -> {
      ServiceDTO.Response response = serviceMapper.toResponseDTO(service);
      response.setContacts(contactService.getContactsByServiceId(service.getId()));
      response.setPhones(phoneService.getPhonesByServiceId(service.getId()));
      response.setLanguages(languageService.getLanguagesByServiceId(service.getId()));
      response.setFunding(fundingService.getFundingByServiceId(service.getId()));
      response.setCostOptions(costOptionService.getCostOptionsByServiceId(service.getId()));
      response.setRequiredDocuments(requiredDocumentService.getRequiredDocumentByServiceId(service.getId()));
      response.setServiceAreas(serviceAreaService.getServiceAreasByServiceId(service.getId()));
      response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(service.getId(), SERVICE_RESOURCE_TYPE));
      return response;
    });
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceById(String id) {
    Service service = serviceRepository.findById(id).orElseThrow();
    Response response = serviceMapper.toResponseDTO(service);
    response.setContacts(contactService.getContactsByServiceId(id));
    response.setPhones(phoneService.getPhonesByServiceId(id));
    response.setLanguages(languageService.getLanguagesByServiceId(id));
    response.setFunding(fundingService.getFundingByServiceId(id));
    response.setCostOptions(costOptionService.getCostOptionsByServiceId(id));
    response.setRequiredDocuments(requiredDocumentService.getRequiredDocumentByServiceId(id));
    response.setServiceAreas(serviceAreaService.getServiceAreasByServiceId(id));
    response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(id, SERVICE_RESOURCE_TYPE));
    return response;
  }

  @Override
  @Transactional
  public Response createService(Request requestDto) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Service service = serviceMapper.toEntity(requestDto);
    Service savedService = serviceRepository.save(service);

    MetadataUtils.createMetadataEntry(
        metadataService,
        savedService.getId(),
        SERVICE_AREA_RESOURCE_TYPE,
        CREATE.name(),
        "service",
        EMPTY_PREVIOUS_VALUE,
        service.getName(),
        "SYSTEM"
    );

    List<ContactDTO.Response> savedContacts = new ArrayList<>();
    if (requestDto.getContacts() != null) {
      for (ContactDTO.Request contactRequest : requestDto.getContacts()) {
        contactRequest.setServiceId(requestDto.getId());
        ContactDTO.Response contactResponse = contactService.createContact(contactRequest);
        savedContacts.add(contactResponse);
      }
    }

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (requestDto.getPhones() != null) {
      for (PhoneDTO.Request phoneRequest : requestDto.getPhones()) {
        phoneRequest.setServiceId(requestDto.getId());
        PhoneDTO.Response phoneResponse = phoneService.createPhone(phoneRequest);
        savedPhones.add(phoneResponse);
      }
    }

    List<LanguageDTO.Response> savedLanguages = new ArrayList<>();
    if (requestDto.getLanguages() != null) {
      for (LanguageDTO.Request languageRequest : requestDto.getLanguages()) {
        languageRequest.setServiceId(requestDto.getId());
        LanguageDTO.Response languageResponse = languageService.createLanguage(languageRequest);
        savedLanguages.add(languageResponse);
      }
    }

    List<FundingDTO.Response> savedFunding = new ArrayList<>();
    if (requestDto.getFunding()!= null) {
      for (FundingDTO.Request fundingRequest : requestDto.getFunding()) {
        fundingRequest.setServiceId(requestDto.getId());
        FundingDTO.Response fundingResponse = fundingService.createFunding(fundingRequest);
        savedFunding.add(fundingResponse);
      }
    }

    List<CostOptionDTO.Response> savedCostOptions = new ArrayList<>();
    if (requestDto.getCostOptions()!= null) {
      for (CostOptionDTO.Request costOptionRequest : requestDto.getCostOptions()) {
        costOptionRequest.setServiceId(requestDto.getId());
        CostOptionDTO.Response costOptionResponse = costOptionService.createCostOption(costOptionRequest);
        savedCostOptions.add(costOptionResponse);
      }
    }

    List<RequiredDocumentDTO.Response> savedDocuments = new ArrayList<RequiredDocumentDTO.Response>();
    if (requestDto.getRequiredDocuments()!= null) {
      for (RequiredDocumentDTO.Request requiredDocumentRequest : requestDto.getRequiredDocuments()) {
        requiredDocumentRequest.setServiceId(requestDto.getId());
        RequiredDocumentDTO.Response requiredDocumentResponse = requiredDocumentService.createRequiredDocument(requiredDocumentRequest);
        savedDocuments.add(requiredDocumentResponse);
      }
    }

    List<ServiceAreaDTO.Response> savedServiceAreas = new ArrayList<>();
    if (requestDto.getServiceAreas()!= null) {
      for (ServiceAreaDTO.Request serviceAreaRequest : requestDto.getServiceAreas()) {
        serviceAreaRequest.setServiceId(requestDto.getId());
        ServiceAreaDTO.Response serviceAreaResponse = serviceAreaService.createServiceArea(serviceAreaRequest);
        savedServiceAreas.add(serviceAreaResponse);
      }
    }

    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(requestDto.getId(), SERVICE_RESOURCE_TYPE);
    Response response = serviceMapper.toResponseDTO(savedService);
    response.setContacts(savedContacts);
    response.setPhones(savedPhones);
    response.setLanguages(savedLanguages);
    response.setFunding(savedFunding);
    response.setCostOptions(savedCostOptions);
    response.setRequiredDocuments(savedDocuments);
    response.setServiceAreas(savedServiceAreas);
    response.setMetadata(metadata);
    return serviceMapper.toResponseDTO(savedService);
  }
}
