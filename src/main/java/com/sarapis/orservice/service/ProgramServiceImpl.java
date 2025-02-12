package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.upsert.UpsertProgramDTO;
import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {
    private final OrganizationRepository organizationRepository;
    private final ProgramRepository programRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    public ProgramServiceImpl(OrganizationRepository organizationRepository,
                              ProgramRepository programRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.programRepository = programRepository;
        this.organizationRepository = organizationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ProgramDTO> getAllPrograms() {
        List<ProgramDTO> programDTOs = this.programRepository.findAll()
                .stream().map(Program::toDTO).toList();
        programDTOs.forEach(this::addRelatedData);
        return programDTOs;
    }

    @Override
    public ProgramDTO getProgramDTOById(String programId) {
        Program program = this.programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found."));
        ProgramDTO programDTO = program.toDTO();
        this.addRelatedData(programDTO);
        return programDTO;
    }

    @Override
    public ProgramDTO createProgram(UpsertProgramDTO upsertProgramDTO) {
        Program program = upsertProgramDTO.create();

        Organization organization = this.organizationRepository.findById(upsertProgramDTO.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));
        program.setOrganization(organization);

        Program createdProgram = this.programRepository.save(program);
        organization.getPrograms().add(createdProgram);
        this.organizationRepository.save(organization);
        return this.getProgramDTOById(createdProgram.getId());
    }

    @Override
    public ProgramDTO updateProgram(String programId, ProgramDTO programDTO) {
        Program program = this.programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found."));

        program.setId(programDTO.getId());
        program.setName(programDTO.getName());
        program.setAlternateName(programDTO.getAlternateName());
        program.setDescription(programDTO.getDescription());

        Program updatedProgram = this.programRepository.save(program);
        return this.getProgramDTOById(updatedProgram.getId());
    }

    @Override
    public void deleteProgram(String programId) {
        Program program = this.programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found."));
        this.attributeService.deleteRelatedAttributes(program.getId());
        this.metadataService.deleteRelatedMetadata(program.getId());
        this.programRepository.delete(program);
    }

    private void addRelatedData(ProgramDTO programDTO) {
        programDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(programDTO.getId()));
        programDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(programDTO.getId()));
    }
}
