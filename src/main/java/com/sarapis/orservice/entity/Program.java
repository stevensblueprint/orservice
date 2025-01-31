package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "program")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Program {
    //================================================================================
    // Methods
    //================================================================================

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description", nullable = false)
    private String description;

    //================================================================================
    // Methods
    //================================================================================

    @OneToMany(mappedBy = "program")
    private List<Service> services = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    //================================================================================
    // Methods
    //================================================================================

    @PreRemove
    public void preRemove() {
        // Sets optional foreign keys to null since we're not using CascadeType.ALL
        for (Service service : services) {
            service.setProgram(null);
        }
    }

    public ProgramDTO toDTO() {
        return ProgramDTO.builder()
                .id(this.id)
                .organizationId(this.organization.getId())
                .name(this.name)
                .alternateName(this.alternateName)
                .description(this.description)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
