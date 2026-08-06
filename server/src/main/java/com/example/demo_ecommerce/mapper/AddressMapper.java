package com.example.demo_ecommerce.mapper;

import com.example.demo_ecommerce.dto.request.CreateAddressRequest;
import com.example.demo_ecommerce.dto.request.UpdateAddressRequest;
import com.example.demo_ecommerce.dto.response.AddressDetailResponse;
import com.example.demo_ecommerce.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    Address toAddress(CreateAddressRequest createAddressRequest);

    AddressDetailResponse toAddressDetailResponse(Address address);

    @Mapping(target = "isDefault", ignore = true)
    void updateAddress(UpdateAddressRequest request, @MappingTarget Address address);
}
