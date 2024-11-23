package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.AddressDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "attention")
    private String attention;

    @Column(name = "address_1", nullable = false)
    private String address_1;

    @Column(name = "address_2")
    private String address_2;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "state_province", nullable = false)
    private String stateProvince;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    public AddressDTO toDTO() {
        return AddressDTO.builder()
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
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
