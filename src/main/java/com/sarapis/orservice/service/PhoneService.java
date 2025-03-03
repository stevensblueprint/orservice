package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;
import java.util.List;

public interface PhoneService {
  PhoneDTO.Response createPhone(PhoneDTO.Request dto);
  List<PhoneDTO.Response> getPhonesByOrganizationId(String organizationId);
}
