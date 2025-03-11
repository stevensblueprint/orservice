package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.Metadata.UPDATE;
import static com.sarapis.orservice.utils.MetadataUtils.DEFAULT_CREATED_BY;
import static com.sarapis.orservice.utils.MetadataUtils.FUNDING_RESOURCE_TYPE;

import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.FundingDTO.Request;
import com.sarapis.orservice.dto.FundingDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.FundingMapper;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.repository.FundingRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FundingServiceImpl implements FundingService{
  private final FundingRepository fundingRepository;
  private final FundingMapper fundingMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createFunding(Request fundingDto) {
    if (fundingDto.getId() == null || fundingDto.getId().trim().isEmpty()) {
      fundingDto.setId(UUID.randomUUID().toString());
    }
    Funding funding = fundingMapper.toEntity(fundingDto);
    Funding savedFunding = fundingRepository.save(funding);
    metadataService.createMetadata(
        null,
        savedFunding,
        FUNDING_RESOURCE_TYPE,
        CREATE,
        DEFAULT_CREATED_BY
    );
    FundingDTO.Response response = fundingMapper.toResponseDTO(savedFunding);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedFunding.getId(), FUNDING_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getFundingByOrganizationId(String organizationId) {
    List<Funding> fundingList = fundingRepository.findByOrganizationId(organizationId);
    List<FundingDTO.Response> fundingDtos =  fundingList.stream().map(fundingMapper::toResponseDTO).toList();
    fundingDtos  = fundingDtos.stream().peek(funding -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          funding.getId(), FUNDING_RESOURCE_TYPE
      );
      funding.setMetadata(metadata);
    }).toList();
    return fundingDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getFundingByServiceId(String serviceId) {
    List<Funding> fundingList = fundingRepository.findByServiceId(serviceId);
    List<FundingDTO.Response> fundingDtos =  fundingList.stream().map(fundingMapper::toResponseDTO).toList();
    fundingDtos  = fundingDtos.stream().peek(funding -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          funding.getId(), FUNDING_RESOURCE_TYPE
      );
      funding.setMetadata(metadata);
    }).toList();
    return fundingDtos;
  }

  @Override
  @Transactional
  public Response updateFunding(String id, Request fundingDto) {
    Funding funding = fundingRepository.findById(id).orElseThrow();
    FundingDTO.Response previousState = fundingMapper.toResponseDTO(funding);
    Funding updatedFunding = fundingRepository.save(funding);
    FundingDTO.Response updatedState = fundingMapper.toResponseDTO(updatedFunding);
    metadataService.createMetadata(
        previousState,
        updatedFunding,
        FUNDING_RESOURCE_TYPE,
        UPDATE,
        DEFAULT_CREATED_BY
    );
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        updatedFunding.getId(), FUNDING_RESOURCE_TYPE
    );
    updatedState.setMetadata(metadata);
    return updatedState;
  }
}
