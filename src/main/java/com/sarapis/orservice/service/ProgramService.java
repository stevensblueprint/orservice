package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import java.util.List;

public interface ProgramService {
    List<ProgramDTO> getAllPrograms();
    ProgramDTO getProgramById(String id);
    ProgramDTO createProgram(ProgramDTO programDTO);
    ProgramDTO updateProgram(String id, ProgramDTO programDTO);
    void deleteProgram(String id);
}
