package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.AddressType;
import com.sarapis.orservice.entity.core.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String id;
    private Location location;
    private String attention;
    private String address_1;
    private String address_2;
    private String city;
    private String region;
    private String stateProvince;
    private String postalCode;
    private String country;
    private AddressType addressType;
}
