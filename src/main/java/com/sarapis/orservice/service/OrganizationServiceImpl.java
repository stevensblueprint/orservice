package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final UrlService urlService;
  private final FundingService fundingService;
  private final ContactService contactService;
  private final PhoneService phoneService;
  private final ProgramService programService;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllOrganizations(String search, Boolean full_service,
      Boolean full, String taxonomyTerm, String taxonomyId, Integer page, Integer perPage,
      String format) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

    // Map each Organization entity to a DTO and populate fields
    Page<OrganizationDTO.Response> dtoPage = organizationPage.map(organization -> {
      OrganizationDTO.Response response = organizationMapper.toResponseDTO(organization);
      List<UrlDTO.Response> urls = urlService.getUrlsByOrganizationId(organization.getId());
      List<FundingDTO.Response> fundingList = fundingService.getFundingByOrganizationId(organization.getId());
      List<ContactDTO.Response> contacts = contactService.getContactsByOrganizationId(organization.getId());
      List<PhoneDTO.Response> phones = phoneService.getPhonesByOrganizationId(organization.getId());
      List<ProgramDTO.Response> programs = programService.getProgramsByOrganizationId(organization.getId());
      response.setAdditionalWebsites(urls);
      response.setFunding(fundingList);
      response.setContacts(contacts);
      response.setPhones(phones);
      response.setPrograms(programs);
      return response;
    });

    return PaginationDTO.fromPage(dtoPage);
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    Response response = organizationMapper.toResponseDTO(organization);
    List<UrlDTO.Response> urls = urlService.getUrlsByOrganizationId(organization.getId());
    List<FundingDTO.Response> fundingList = fundingService.getFundingByOrganizationId(organization.getId());
    List<ContactDTO.Response> contacts = contactService.getContactsByOrganizationId(organization.getId());
    List<PhoneDTO.Response> phones = phoneService.getPhonesByOrganizationId(organization.getId());
    List<ProgramDTO.Response> programs = programService.getProgramsByOrganizationId(organization.getId());
    response.setAdditionalWebsites(urls);
    response.setFunding(fundingList);
    response.setContacts(contacts);
    response.setPhones(phones);
    response.setPrograms(programs);
    return response;
  }

  @Override
  @Transactional
  public Response createOrganization(Request requestDto) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Organization organization = organizationMapper.toEntity(requestDto);
    Organization savedOrganization = organizationRepository.save(organization);

    List<UrlDTO.Response> savedUrls = new ArrayList<>();
    if (requestDto.getAdditionalWebsites() != null) {
      for (UrlDTO.Request urlDTO : requestDto.getAdditionalWebsites()) {
        urlDTO.setOrganizationId(savedOrganization.getId());
        UrlDTO.Response savedUrl = urlService.createUrl(urlDTO);
        savedUrls.add(savedUrl);
      }
    }

    List<FundingDTO.Response> savedFunding = new ArrayList<>();
    if (requestDto.getFunding() != null) {
      for (FundingDTO.Request fundingDTO : requestDto.getFunding()) {
        fundingDTO.setOrganizationId(savedOrganization.getId());
        FundingDTO.Response savedFundingItem = fundingService.createFunding(fundingDTO);
        savedFunding.add(savedFundingItem);
      }
    }

    List<ContactDTO.Response> savedContacts = new ArrayList<>();
    if (requestDto.getContacts() != null) {
      for (ContactDTO.Request contactDTO : requestDto.getContacts()) {
        contactDTO.setOrganizationId(savedOrganization.getId());
        ContactDTO.Response savedContact = contactService.createContact(contactDTO);
        savedContacts.add(savedContact);
      }
    }

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (requestDto.getPhones()!= null) {
      for (PhoneDTO.Request phoneDTO : requestDto.getPhones()) {
        phoneDTO.setOrganizationId(savedOrganization.getId());
        PhoneDTO.Response savedPhone = phoneService.createPhone(phoneDTO);
        savedPhones.add(savedPhone);
      }
    }

    List<ProgramDTO.Response> savedPrograms = new ArrayList<>();
    if (requestDto.getPrograms() != null) {
      for (ProgramDTO.Request programDTO : requestDto.getPrograms()) {
        programDTO.setOrganizationId(savedOrganization.getId());
        ProgramDTO.Response savedProgram = programService.createProgram(programDTO);
        savedPrograms.add(savedProgram);
      }
    }

    Response response = organizationMapper.toResponseDTO(savedOrganization);
    response.setAdditionalWebsites(savedUrls);
    response.setFunding(savedFunding);
    response.setContacts(savedContacts);
    response.setPhones(savedPhones);
    response.setPrograms(savedPrograms);
    return response;
  }
}
