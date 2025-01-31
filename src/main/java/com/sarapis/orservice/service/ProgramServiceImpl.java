package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.upsert.UpsertProgramDTO;
import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
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
                .orElseThrow(() -> new RuntimeException("Program not found."));
        ProgramDTO programDTO = program.toDTO();
        this.addRelatedData(programDTO);
        return programDTO;
    }

    @Override
    public ProgramDTO createProgram(UpsertProgramDTO upsertProgramDTO) {
        Program program = upsertProgramDTO.create();

        Organization organization = this.organizationRepository.findById(upsertProgramDTO.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        program.setOrganization(organization);

        Program createdProgram = this.programRepository.save(program);
        organization.getPrograms().add(createdProgram);
        this.organizationRepository.save(organization);
        return this.getProgramDTOById(createdProgram.getId());
    }

    @Override
    public ProgramDTO updateProgram(String programId, UpsertProgramDTO upsertProgramDTO) {
        Program program = this.programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found."));
        Program updatedProgram = upsertProgramDTO.merge(program);
        updatedProgram.setId(programId);

        if (upsertProgramDTO.getOrganizationId() != null) {
            Organization organization = this.organizationRepository.findById(upsertProgramDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
            organization.getPrograms().add(updatedProgram);
            this.organizationRepository.save(organization);
            program.getOrganization().getPrograms().remove(program);
            updatedProgram.setOrganization(organization);
        }

        this.programRepository.save(updatedProgram);
        return this.getProgramDTOById(updatedProgram.getId());
    }

    @Override
    public void deleteProgram(String programId) {
        Program program = this.programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found."));
        this.attributeService.deleteRelatedAttributes(program.getId());
        this.metadataService.deleteRelatedMetadata(program.getId());
        this.programRepository.delete(program);
    }

    private void addRelatedData(ProgramDTO programDTO) {
        programDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(programDTO.getId()));
        programDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(programDTO.getId()));
    }
}
