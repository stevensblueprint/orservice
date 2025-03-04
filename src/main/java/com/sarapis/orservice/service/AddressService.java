package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AddressDTO;
import java.util.List;

public interface AddressService {
  AddressDTO.Response createAddress(AddressDTO.Request request);
  List<AddressDTO.Response> getAddressesByLocationId(String locationId);
}
