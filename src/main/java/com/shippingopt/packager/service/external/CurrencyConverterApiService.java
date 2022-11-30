package com.shippingopt.packager.service.external;

import com.shippingopt.packager.dto.external.response.ExchangeRateResponse;
import com.shippingopt.packager.dto.request.PackageSpecs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Locale;

@Service
public class CurrencyConverterApiService {

    @Value("${currency-converter-api.key}")
    private String apiKey;

    @Value("${currency-converter-api.base-url}")
    private String baseUrl;

    @Autowired
    @Qualifier("currencyConvRestTemplate")
    private RestTemplate currencyConvRestTemplate;

    private String templateCall = "%s?symbols=%s&base=%s";

    public void adjustPrices(String baseCurrency, PackageSpecs packageSpecs) {
        BigDecimal exchangeRate;
        ExchangeRateResponse exchangeRateResponse = this.getExchangeRate(baseCurrency, packageSpecs.getCurrency());
        if (exchangeRateResponse != null && exchangeRateResponse.getRates() != null && !exchangeRateResponse.getRates().isEmpty()) {
            exchangeRate = exchangeRateResponse.getRates().get(baseCurrency);
            // the service returns a map with the conversion rate compared to the base currency provided
            packageSpecs.getItems().forEach(x -> x.setPrice(x.getPrice().multiply(exchangeRate, new MathContext(4, RoundingMode.HALF_UP))));
        }
    }

    public ExchangeRateResponse getExchangeRate(String from, String to) {
        String symbols = StringUtils.join(Arrays.asList(from, to), ",");
        String compositeUrl = String.format(Locale.US, templateCall, baseUrl, symbols, to);
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ExchangeRateResponse> response = currencyConvRestTemplate.exchange(compositeUrl, HttpMethod.GET, entity, ExchangeRateResponse.class);
        return response.getBody();
    }


}
