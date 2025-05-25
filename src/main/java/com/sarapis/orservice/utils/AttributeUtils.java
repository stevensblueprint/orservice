package com.sarapis.orservice.utils;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.mapper.AttributeMapper;
import com.sarapis.orservice.model.Attribute;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AttributeUtils {
  public static final String ORGANIZATION_LINK_TYPE = "ORGANIZATION";
  public static final String URL_LINK_TYPE = "URL";
  public static final String PROGRAM_LINK_TYPE = "PROGRAM";
  public static final String PHONE_LINK_TYPE = "PHONE";
  public static final String ORGANIZATION_IDENTIFIER_LINK_TYPE = "ORGANIZATION_IDENTIFIER";
  public static final String FUNDING_LINK_TYPE = "FUNDING_RESOURCE";
  public static final String CONTACT_LINK_TYPE = "CONTACT";
  public static final String LANGUAGE_LINK_TYPE = "LANGUAGE";
  public static final String ACCESSIBILITY_LINK_TYPE = "ACCESSIBILITY";
  public static final String ADDRESS_LINK_TYPE = "ADDRESS";
  public static final String SCHEDULE_LINK_TYPE = "SCHEDULE";
  public static final String LOCATION_LINK_TYPE = "LOCATION";
  public static final String SERVICE_AREA_LINK_TYPE = "SERVICE_AREA";
  public static final String COST_OPTION_LINK_TYPE = "COST_OPTION";
  public static final String SERVICE_LINK_TYPE = "SERVICE";
  public static final String REQUIRED_DOCUMENT_LINK_TYPE = "REQUIRED_DOCUMENT";
  public static final String SERVICE_AT_LOCATION_LINK_TYPE = "SERVICE_AT_LOCATION";
  public static final String TAXONOMY_TERM_LINK_TYPE = "TAXONOMY_TERM";
  public static final String TAXONOMY_LINK_TYPE = "TAXONOMY";
  public static final String UNIT_LINK_TYPE = "UNIT";
  public static final String SERVICE_CAPACITY_LINK_TYPE = "SERVICE_CAPACITY";

  public static void saveAttributes(AttributeRepository repository,
      AttributeMapper mapper, List<AttributeDTO.Request> attributes, String linkType) {
    List<Attribute> entities = attributes
        .stream()
        .map(mapper::toEntity)
        .peek(attribute -> attribute.setLinkType(linkType))
        .toList();
    repository.saveAll(entities);
  }

  public static <T, R> void enrichAttributes(
      T resource,
      R response,
      Function<T, String> idExtractor,
      BiConsumer<R, List<AttributeDTO.Response>> attributeSetter,
      String linkType,
      AttributeService attributeService
  ) {
    List<AttributeDTO.Response> ad = attributeService.getAttributeByLinkIdAndLinkType(
        idExtractor.apply(resource),
        linkType
    );
    attributeSetter.accept(response, ad);
  }
}
