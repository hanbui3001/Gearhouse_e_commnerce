package com.example.demo_ecommerce.dto.response;


public record AddressDetailResponse(
        String id,
        String receiverName,
        String phoneNumber,
        String addressLine,
        String ward,
        String district,
        String province,
        Boolean isDefault
) {
}
