package com.shippingopt.packager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageSpecs {
    @NotNull
    @Size(min = 1, max = 15)
    @Valid
    private List<Item> items;
    @NotNull
    @DecimalMax("100")
    private BigDecimal maxPkgWeight;

    @Schema(type = "string", example = "EUR", required = false)
    private String currency = "EUR";
}
