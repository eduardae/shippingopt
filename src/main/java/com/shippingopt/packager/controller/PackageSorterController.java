package com.shippingopt.packager.controller;

import com.shippingopt.packager.dto.request.PackageSpecs;
import com.shippingopt.packager.exception.PriceValidationException;
import com.shippingopt.packager.exception.NoSuitableItemException;
import com.shippingopt.packager.service.PackageSorterService;
import com.shippingopt.packager.service.external.CurrencyConverterApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("package-sort")
public class PackageSorterController {

    @Autowired
    private PackageSorterService packageSorterService;

    @Autowired
    private CurrencyConverterApiService converterApiService;

    @Value("${base-currency}")
    private String baseCurrency;

    @PostMapping(value = "/", produces = "application/json")
    @Validated
    public ResponseEntity<List<Long>> packageSort(@RequestBody @Valid PackageSpecs packageSpecs) throws PriceValidationException, NoSuitableItemException {
        if (!packageSpecs.getCurrency().equals(baseCurrency)) {
            converterApiService.adjustPrices(baseCurrency, packageSpecs);
        }
        packageSorterService.validatePrice(packageSpecs, baseCurrency);
        List<Long> itemIds = packageSorterService.getItemIdsFromPkgSpecs(packageSpecs);
        return ResponseEntity.ok(itemIds);
    }

}
