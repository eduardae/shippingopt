package com.shippingopt.packager.dto.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExchangeRateResponse {
    private HashMap<String, BigDecimal> rates;
}