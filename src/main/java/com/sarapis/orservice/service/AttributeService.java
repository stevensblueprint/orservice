package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import java.util.List;

public interface AttributeService {
  List<AttributeDTO.Response> getAttributeByLinkIdAndLinkType(String linkId, String linkType);
}
