package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.upsert.UpsertProgramDTO;

import java.util.List;

public interface ProgramService {
    List<ProgramDTO> getAllPrograms();

    ProgramDTO getProgramDTOById(String programId);

    ProgramDTO createProgram(UpsertProgramDTO upsertProgramDTO);

    ProgramDTO updateProgram(String programId, ProgramDTO programDTO);

    void deleteProgram(String programId);
}
