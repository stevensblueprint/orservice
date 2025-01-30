package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.entity.core.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "organization_identifier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationIdentifier {
    //================================================================================
    // Attributes
    //================================================================================

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "identifier_scheme")
    private String identifierScheme;

    @Column(name = "identifier_type", nullable = false)
    private String identifierType;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    //================================================================================
    // Relations
    //================================================================================

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    //================================================================================
    // Methods
    //================================================================================

    public OrganizationIdentifierDTO toDTO() {
        return OrganizationIdentifierDTO.builder()
                .id(this.id)
                .organizationId(this.organization.getId())
                .identifierScheme(this.identifierScheme)
                .identifierType(this.identifierType)
                .identifier(this.identifier)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
