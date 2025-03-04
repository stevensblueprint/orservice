package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.CostOptionDTO;
import java.util.List;

public interface CostOptionService {
  CostOptionDTO.Response createCostOption(CostOptionDTO.Request request);
  List<CostOptionDTO.Response> getCostOptionsByServiceId(String serviceId);
}
