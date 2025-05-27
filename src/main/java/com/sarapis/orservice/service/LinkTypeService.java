package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LinkTypeDTO;
import com.sarapis.orservice.dto.PaginationDTO;

public interface LinkTypeService {
  PaginationDTO<LinkTypeDTO.Response> getAllLinkTypes(
      Integer page,
      Integer perPage
  );

  LinkTypeDTO.Response getLinkTypeById(String id);
  LinkTypeDTO.Response createLinkType(LinkTypeDTO.Request request);
  LinkTypeDTO.Response updateLinkType(String id, LinkTypeDTO.Request request);
  void deleteLinkType(String id);
}
