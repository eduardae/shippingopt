package com.shippingopt.packager.service.external;

import com.shippingopt.packager.TestUtils;
import com.shippingopt.packager.dto.external.response.ExchangeRateResponse;
import com.shippingopt.packager.dto.request.PackageSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;

@SpringBootTest
public class CurrencyConverterApiServiceTest {

    @Autowired
    private CurrencyConverterApiService currencyConverterApiService;

    @MockBean(name = "currencyConvRestTemplate")
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup () {
        HashMap<String, BigDecimal> exchangeRatesMap = new HashMap<>(2);
        exchangeRatesMap.put("USD", BigDecimal.valueOf(1));
        exchangeRatesMap.put("EUR", BigDecimal.valueOf(0.96));
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setRates(exchangeRatesMap);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(),  Mockito.eq(ExchangeRateResponse.class)))
                .thenReturn(
                       ResponseEntity.ok(response)
                );
    }

    @Test
    public void adjustPrices() {
        PackageSpecs packageSpecs = TestUtils.getOkPackageSpecs("USD");
        BigDecimal originalValue = BigDecimal.valueOf(packageSpecs.getItems().get(0).getPrice().doubleValue());
        currencyConverterApiService.adjustPrices("EUR", packageSpecs);
        Assertions.assertEquals(originalValue.multiply(BigDecimal.valueOf(0.96), new MathContext(4, RoundingMode.HALF_UP)), packageSpecs.getItems().get(0).getPrice());
    }

}
