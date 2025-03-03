package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import java.util.List;

public interface ProgramService {
  ProgramDTO.Response createProgram(ProgramDTO.Request dto);
  List<ProgramDTO.Response> getProgramsByOrganizationId(String organizationId);
}
