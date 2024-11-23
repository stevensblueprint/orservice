package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Contact;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDTO {
    private String id;
    private String name;
    private String title;
    private String department;
    private String email;
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Contact toEntity() {
        return Contact.builder()
                .id(this.id)
                .name(this.name)
                .title(this.title)
                .department(this.department)
                .email(this.email)
                .phones(this.phones.stream().map(PhoneDTO::toEntity).toList())
                .build();
    }
}
