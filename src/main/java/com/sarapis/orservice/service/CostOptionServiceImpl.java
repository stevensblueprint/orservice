package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.dto.CostOptionDTO.Request;
import com.sarapis.orservice.dto.CostOptionDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.CostOptionMapper;
import com.sarapis.orservice.model.CostOption;
import com.sarapis.orservice.repository.CostOptionRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CostOptionServiceImpl implements CostOptionService{
  private final CostOptionRepository costOptionRepository;
  private final CostOptionMapper costOptionMapper;
  private final MetadataService metadataService;
  @Override
  public Response createCostOption(Request request) {
    if (request.getId() == null || request.getId().trim().isEmpty()) {
      request.setId(UUID.randomUUID().toString());
    }
    CostOption costOption = costOptionMapper.toEntity(request);
    CostOption savedCostOption = costOptionRepository.save(costOption);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedCostOption.getId(),
        COST_OPTION_RESOURCE_TYPE,
        CREATE.name(),
        "cost_option",
        EMPTY_PREVIOUS_VALUE,
        request.getOption(),
        "SYSTEM"
    );
    CostOptionDTO.Response response = costOptionMapper.toResponseDTO(savedCostOption);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedCostOption.getId(), COST_OPTION_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  public List<Response> getCostOptionsByServiceId(String serviceId) {
    List<CostOption> costOptions = costOptionRepository.findByServiceId(serviceId);
    List<CostOptionDTO.Response> costOptionDtos = costOptions.stream().map(costOptionMapper::toResponseDTO).toList();
    costOptionDtos = costOptionDtos.stream().peek(costOption -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          costOption.getId(), COST_OPTION_RESOURCE_TYPE
      );
      costOption.setMetadata(metadata);
    }).toList();
    return costOptionDtos;
  }
}
