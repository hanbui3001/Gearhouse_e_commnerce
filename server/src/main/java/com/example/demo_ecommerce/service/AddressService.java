package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.CreateAddressRequest;
import com.example.demo_ecommerce.dto.request.UpdateAddressRequest;
import com.example.demo_ecommerce.dto.response.AddressDetailResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;

public interface AddressService {
    AddressDetailResponse createAddress(String userId, CreateAddressRequest address);

    PageResponse<AddressDetailResponse> findAddressesByUserId(String userId, int page, int size);

    AddressDetailResponse updateAddress(String userId, String addressId, UpdateAddressRequest request);

    void changeDefaultAddress(String userId, String addressId);

    void deleteAddress(String userId, String addressId);
}
