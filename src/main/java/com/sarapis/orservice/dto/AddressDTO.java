package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.entity.AddressType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private String id;
    private String attention;
    private String address_1;
    private String address_2;
    private String city;
    private String region;
    private String stateProvince;
    private String postalCode;
    private String country;
    private AddressType addressType;
    private List<AttributeDTO> attributes = new ArrayList<>();
    private List<MetadataDTO> metadata = new ArrayList<>();

    public Address toEntity() {
        return Address.builder()
                .id(this.id)
                .attention(this.attention)
                .address_1(this.address_1)
                .address_2(this.address_2)
                .city(this.city)
                .region(this.region)
                .stateProvince(this.stateProvince)
                .postalCode(this.postalCode)
                .country(this.country)
                .addressType(this.addressType)
                .attributes(this.attributes.stream().map(AttributeDTO::toEntity).toList())
                .metadata(this.metadata.stream().map(MetadataDTO::toEntity).toList())
                .build();
    }
}
