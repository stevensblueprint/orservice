package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.CONTACT_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.COST_OPTION_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.model.CostOption;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class CostOptionMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "service.id", source = "serviceId")
  public abstract CostOption toEntity(CostOptionDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  public abstract CostOptionDTO.Response toResponseDTO(CostOption entity);

  @AfterMapping
  protected void toEntity(CostOptionDTO.Request dto, @MappingTarget() CostOption entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        COST_OPTION_LINK_TYPE
    );
  }

  protected CostOptionDTO.Response toResponseDTO(CostOption entity, MetadataService metadataService) {
    CostOptionDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        CostOption::getId,
        CostOptionDTO.Response::setMetadata,
        COST_OPTION_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        CostOption::getId,
        CostOptionDTO.Response::setAttributes,
        CONTACT_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
