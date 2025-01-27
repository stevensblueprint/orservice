package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.entity.AddressType;
import com.sarapis.orservice.entity.core.Location;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private String id;

    private String locationId;

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

    public Address toEntity(Location location) {
        return Address.builder()
                .id(this.id == null ? UUID.randomUUID().toString() : this.id)
                .location(location)
                .attention(this.attention)
                .address_1(this.address_1)
                .address_2(this.address_2)
                .city(this.city)
                .region(this.region)
                .stateProvince(this.stateProvince)
                .postalCode(this.postalCode)
                .country(this.country)
                .addressType(this.addressType)
                .build();
    }
}
