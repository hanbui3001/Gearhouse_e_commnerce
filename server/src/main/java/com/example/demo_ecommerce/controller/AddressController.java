package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.CreateAddressRequest;
import com.example.demo_ecommerce.dto.request.UpdateAddressRequest;
import com.example.demo_ecommerce.dto.response.AddressDetailResponse;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddressDetailResponse> createAddress(@AuthenticationPrincipal Jwt jwt,
                                                             @RequestBody @Valid CreateAddressRequest request) {
        String userId = jwt.getSubject();
        var data = addressService.createAddress(userId, request);
        return ApiResponse.<AddressDetailResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("create address successfully")
                .data(data)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<PageResponse<AddressDetailResponse>> getAllAddress(@AuthenticationPrincipal Jwt jwt,
                                                                          @RequestParam(required = false, defaultValue = "1") int page,
                                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        var data = addressService.findAddressesByUserId(jwt.getSubject(), page, size);
        return ApiResponse.<PageResponse<AddressDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("get list addresses")
                .data(data)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressDetailResponse> updateAddress(@PathVariable String id,
                                                            @AuthenticationPrincipal Jwt jwt,
                                                            @RequestBody @Valid UpdateAddressRequest request) {
        var data = addressService.updateAddress(jwt.getSubject(), id, request);

        return ApiResponse.<AddressDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("update address successfully")
                .data(data)
                .build();
    }

    @PutMapping("/default-address/{id}")
    public ApiResponse<Void> setDefaultAddress(@PathVariable String id,
                                               @AuthenticationPrincipal Jwt jwt) {
        addressService.changeDefaultAddress(jwt.getSubject(), id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("set default address successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable String id,
                                           @AuthenticationPrincipal Jwt jwt) {
        addressService.deleteAddress(jwt.getSubject(), id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("delete address successfully")
                .build();
    }
}
