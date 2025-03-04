package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface ContactMapper {
  Contact toEntity(ContactDTO.Request dto);

  ContactDTO.Response toResponseDTO(Contact entity);
}
