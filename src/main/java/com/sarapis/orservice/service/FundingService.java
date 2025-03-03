package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FundingDTO;
import java.util.List;

public interface FundingService {
  FundingDTO.Response createFunding(FundingDTO.Request request);
  List<FundingDTO.Response> getFundingByOrganizationId(String organizationId);
}
