package com.shippingopt.packager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemToRatio {
    private Item item;
    private BigDecimal priceToWeightRatio;
}
