package com.example.demo_ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileResponse(
        String fileName,
        String fileType,
        Double size,
        String url,
        Integer displayOrder
) {
}
