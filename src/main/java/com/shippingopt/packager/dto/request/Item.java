package com.shippingopt.packager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotNull
    private Long id;

    @NotNull
    @Max(100)
    @DecimalMin("0.01")
    @Schema(example = "20")
    private BigDecimal weight;

    @NotNull
    @DecimalMin("0.01")
    @Schema(description = "max 100 EUR", example = "10")
    private BigDecimal price;
}
