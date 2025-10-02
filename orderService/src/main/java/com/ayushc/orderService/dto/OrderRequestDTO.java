package com.ayushc.orderService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrderRequestDTO (
        @NotBlank @Size(min=1, max=10) String id,
        @NotBlank String itemName,
        @NotBlank @Pattern(regexp = "placed|shipped|delivered") String status
) {}
