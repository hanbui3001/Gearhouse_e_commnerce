package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.CreateAddressRequest;
import com.example.demo_ecommerce.dto.request.UpdateAddressRequest;
import com.example.demo_ecommerce.dto.response.AddressDetailResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.mapper.AddressMapper;
import com.example.demo_ecommerce.model.Address;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.AddressRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.AddressService;
import com.example.demo_ecommerce.utils.PageResponseUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDetailResponse createAddress(String userId, CreateAddressRequest addressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean firstAddress = !addressRepository.existsByUser_Id(userId);

        Address address = addressMapper.toAddress(addressRequest);
        address.setIsDefault(firstAddress);
        address.setUser(user);
        address = addressRepository.save(address);
        return addressMapper.toAddressDetailResponse(address);

    }

    @Override
    public PageResponse<AddressDetailResponse> findAddressesByUserId(String userId, int page, int size) {
        page = PageResponseUtils.normalizePage(page);
        size = PageResponseUtils.normalizeSize(size);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Address> addressPage = addressRepository.findAllByUser_Id(userId, pageable);
        List<AddressDetailResponse> addressList = addressPage.getContent()
                .stream()
                .map(addressMapper::toAddressDetailResponse)
                .toList();

        return PageResponse.<AddressDetailResponse>builder()
                .currentPage(addressPage.getNumber() + 1)
                .pageSize(addressPage.getSize())
                .totalPages(addressPage.getTotalPages())
                .total(addressPage.getTotalElements())
                .data(addressList)
                .build();

    }

    @Override
    @Transactional
    public AddressDetailResponse updateAddress(String userId, String addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        addressMapper.updateAddress(request, address);

        return addressMapper.toAddressDetailResponse(address);

    }

    @Transactional
    @Override
    public void changeDefaultAddress(String userId, String addressId) {
        Address selectedAddress = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        if (Boolean.TRUE.equals(selectedAddress.getIsDefault())) {
            return;
        }
        addressRepository.findByUser_IdAndIsDefaultTrue(userId)
                .ifPresent(address -> address.setIsDefault(false));

        selectedAddress.setIsDefault(true);

    }

    @Override
    @Transactional
    public void deleteAddress(String userId, String addressId) {
        Address currentAddress = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        if (Boolean.TRUE.equals(currentAddress.getIsDefault())) {
            addressRepository.findFirstByUser_IdAndIsDefaultFalseOrderByCreatedAtAsc(userId)
                    .ifPresent(address -> address.setIsDefault(true));
        }
        addressRepository.delete(currentAddress);
    }
}
