package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.upsert.UpsertCostOptionDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceAtLocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceCapacityDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceDTO;
import com.sarapis.orservice.entity.*;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final OrganizationRepository organizationRepository;
    private final UrlRepository urlRepository;
    private final LanguageRepository languageRepository;
    private final FundingRepository fundingRepository;
    private final ProgramRepository programRepository;
    private final RequiredDocumentRepository requiredDocumentRepository;
    private final LocationRepository locationRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final PhoneRepository phoneRepository;
    private final ContactRepository contactRepository;
    private final ScheduleRepository scheduleRepository;
    private final ServiceAreaRepository serviceAreaRepository;
    private final CostOptionRepository costOptionRepository;
    private final ServiceCapacityRepository serviceCapacityRepository;
    private final UnitRepository unitRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              OrganizationRepository organizationRepository,
                              UrlRepository urlRepository,
                              LanguageRepository languageRepository,
                              FundingRepository fundingRepository,
                              ProgramRepository programRepository,
                              RequiredDocumentRepository requiredDocumentRepository,
                              LocationRepository locationRepository,
                              ServiceAtLocationRepository serviceAtLocationRepository,
                              PhoneRepository phoneRepository,
                              ContactRepository contactRepository,
                              ScheduleRepository scheduleRepository,
                              ServiceAreaRepository serviceAreaRepository,
                              CostOptionRepository costOptionRepository,
                              ServiceCapacityRepository serviceCapacityRepository,
                              UnitRepository unitRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.serviceRepository = serviceRepository;
        this.organizationRepository = organizationRepository;
        this.urlRepository = urlRepository;
        this.languageRepository = languageRepository;
        this.fundingRepository = fundingRepository;
        this.programRepository = programRepository;
        this.requiredDocumentRepository = requiredDocumentRepository;
        this.locationRepository = locationRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.phoneRepository = phoneRepository;
        this.contactRepository = contactRepository;
        this.scheduleRepository = scheduleRepository;
        this.serviceAreaRepository = serviceAreaRepository;
        this.costOptionRepository = costOptionRepository;
        this.serviceCapacityRepository = serviceCapacityRepository;
        this.unitRepository = unitRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        List<ServiceDTO> serviceDTOs = this.serviceRepository.findAll().stream()
                .map(com.sarapis.orservice.entity.core.Service::toDTO).toList();
        serviceDTOs.forEach(this::addRelatedData);
        return serviceDTOs;
    }

    @Override
    public ServiceDTO getServiceById(String serviceId) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        ServiceDTO serviceDTO = service.toDTO();
        this.addRelatedData(serviceDTO);
        return serviceDTO;
    }

    @Override
    public ServiceDTO createService(UpsertServiceDTO upsertServiceDTO) {
        com.sarapis.orservice.entity.core.Service service = upsertServiceDTO.create();

        Organization organization = this.organizationRepository.findById(upsertServiceDTO.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        service.setOrganization(organization);

        com.sarapis.orservice.entity.core.Service createdService = this.serviceRepository.save(service);

        createdService.setAdditionalUrls(new ArrayList<>());
        for (String id : upsertServiceDTO.getAdditionalUrls()) {
            Url url = this.urlRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Url not found."));
            url.setOrganization(organization);
            this.urlRepository.save(url);
            createdService.getAdditionalUrls().add(url);
        }

        createdService.setLanguages(new ArrayList<>());
        for (String id : upsertServiceDTO.getLanguages()) {
            Language language = this.languageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Language not found."));
            language.setService(createdService);
            this.languageRepository.save(language);
            createdService.getLanguages().add(language);
        }

        createdService.setFunding(new ArrayList<>());
        for (String id : upsertServiceDTO.getFundings()) {
            Funding funding = this.fundingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Funding not found."));
            funding.setService(createdService);
            this.fundingRepository.save(funding);
            createdService.getFunding().add(funding);
        }

        if (upsertServiceDTO.getProgramId() != null) {
            Program program = this.programRepository.findById(upsertServiceDTO.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found."));
            program.getServices().add(createdService);
            this.programRepository.save(program);
            createdService.setProgram(program);
        }

        createdService.setRequiredDocuments(new ArrayList<>());
        for (String id : upsertServiceDTO.getRequiredDocuments()) {
            RequiredDocument requiredDocument = this.requiredDocumentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Required document not found."));
            requiredDocument.setService(createdService);
            this.requiredDocumentRepository.save(requiredDocument);
            createdService.getRequiredDocuments().add(requiredDocument);
        }

        createdService.setServiceAtLocations(new ArrayList<>());
        for (String id : upsertServiceDTO.getLocations()) {
            Location location = this.locationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            ServiceAtLocation serviceAtLocation = UpsertServiceAtLocationDTO.create(createdService, location);
            ServiceAtLocation createdServiceAtLocation = this.serviceAtLocationRepository.save(serviceAtLocation);
            location.getServiceAtLocations().add(createdServiceAtLocation);
            this.locationRepository.save(location);
            createdService.getServiceAtLocations().add(createdServiceAtLocation);
        }

        createdService.setPhones(new ArrayList<>());
        for (String id : upsertServiceDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
            phone.setService(createdService);
            this.phoneRepository.save(phone);
            createdService.getPhones().add(phone);
        }

        createdService.setContacts(new ArrayList<>());
        for (String id : upsertServiceDTO.getContacts()) {
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contact not found."));
            contact.setService(createdService);
            this.contactRepository.save(contact);
            createdService.getContacts().add(contact);
        }

        createdService.setSchedules(new ArrayList<>());
        for (String id : upsertServiceDTO.getSchedules()) {
            Schedule schedule = this.scheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule not found."));
            schedule.setService(createdService);
            this.scheduleRepository.save(schedule);
            createdService.getSchedules().add(schedule);
        }

        createdService.setServiceAreas(new ArrayList<>());
        for (String id : upsertServiceDTO.getServiceAreas()) {
            ServiceArea serviceArea = this.serviceAreaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Service area not found."));
            serviceArea.setService(createdService);
            this.serviceAreaRepository.save(serviceArea);
            createdService.getServiceAreas().add(serviceArea);
        }

        createdService.setCostOptions(new ArrayList<>());
        for (UpsertCostOptionDTO dto : upsertServiceDTO.getCostOptions()) {
            CostOption costOption = dto.create();
            costOption.setService(createdService);
            CostOption createdCostOption = this.costOptionRepository.save(costOption);
            createdService.getCostOptions().add(createdCostOption);
        }

        createdService.setCapacities(new ArrayList<>());
        for (UpsertServiceCapacityDTO dto : upsertServiceDTO.getServiceCapacities()) {
            Unit unit = this.unitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unit not found."));
            ServiceCapacity serviceCapacity = dto.create();
            serviceCapacity.setService(createdService);
            serviceCapacity.setUnit(unit);
            unit.getServiceCapacities().add(serviceCapacity);
            this.unitRepository.save(unit);
            this.serviceCapacityRepository.save(serviceCapacity);
            createdService.getCapacities().add(serviceCapacity);
        }

        this.serviceRepository.save(createdService);
        return this.getServiceById(createdService.getId());
    }

    @Override
    public ServiceDTO updateService(String serviceId, ServiceDTO serviceDTO) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));

        com.sarapis.orservice.entity.core.Service updatedService = this.serviceRepository.save(service);
        return this.getServiceById(updatedService.getId());
    }

    @Override
    public void deleteService(String serviceId) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        this.attributeService.deleteRelatedAttributes(service.getId());
        this.metadataService.deleteRelatedMetadata(service.getId());
        this.serviceRepository.delete(service);
    }

    private void addRelatedData(ServiceDTO serviceDTO) {
        serviceDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(serviceDTO.getId()));
        serviceDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(serviceDTO.getId()));
    }
}
