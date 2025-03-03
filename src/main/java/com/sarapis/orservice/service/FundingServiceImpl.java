package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FundingDTO.Request;
import com.sarapis.orservice.dto.FundingDTO.Response;
import com.sarapis.orservice.mapper.FundingMapper;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.repository.FundingRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FundingServiceImpl implements FundingService{
  private final FundingRepository fundingRepository;
  private final FundingMapper fundingMapper;

  @Override
  @Transactional
  public Response createFunding(Request fundingDto) {
    if (fundingDto.getId() == null || fundingDto.getId().trim().isEmpty()) {
      fundingDto.setId(UUID.randomUUID().toString());
    }
    Funding funding = fundingMapper.toEntity(fundingDto);
    Funding savedFunding = fundingRepository.save(funding);
    return fundingMapper.toResponseDTO(savedFunding);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getFundingByOrganizationId(String organizationId) {
    List<Funding> fundingList = fundingRepository.findByOrganizationId(organizationId);
    return fundingList.stream().map(fundingMapper::toResponseDTO).collect(Collectors.toList());
  }
}
