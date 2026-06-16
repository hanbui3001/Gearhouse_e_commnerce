package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.enums.Status;
import lombok.Builder;

@Builder
public record ChangeStatusRequest(
        Status status
) {
}
