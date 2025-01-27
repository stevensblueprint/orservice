package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Language;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageDTO {
    private String id;

    private String serviceId;
    private String locationId;
    private String phoneId;

    private String name;
    private String code;
    private String note;

    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Language toEntity(Service service, Location location, Phone phone) {
        return Language.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .service(service)
                .location(location)
                .phone(phone)
                .name(this.name)
                .code(this.code)
                .note(this.note)
                .build();
    }
}
