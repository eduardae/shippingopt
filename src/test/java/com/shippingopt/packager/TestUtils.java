package com.shippingopt.packager;

import com.shippingopt.packager.dto.request.Item;
import com.shippingopt.packager.dto.request.PackageSpecs;

import java.math.BigDecimal;
import java.util.Arrays;

public class TestUtils {

    public static PackageSpecs getOkPackageSpecs(String currency) {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setPrice(BigDecimal.valueOf(60));
        item1.setWeight(BigDecimal.valueOf(20));
        Item item2 = new Item();
        item2.setId(2L);
        item2.setPrice(BigDecimal.valueOf(40));
        item2.setWeight(BigDecimal.valueOf(30));
        Item item3 = new Item();
        item3.setId(3L);
        item3.setPrice(BigDecimal.valueOf(50));
        item3.setWeight(BigDecimal.valueOf(70));
        PackageSpecs specs = new PackageSpecs();
        specs.setItems(Arrays.asList(item1, item2, item3));
        specs.setMaxPkgWeight(BigDecimal.valueOf(60));
        specs.setCurrency(currency);
        return specs;
    }

    public static PackageSpecs getNoSuitablePackageSpecs() {
        Item nonSuitableItem = new Item();
        nonSuitableItem.setPrice(BigDecimal.valueOf(50));
        nonSuitableItem.setWeight(BigDecimal.valueOf(50));
        PackageSpecs specs = new PackageSpecs();
        specs.setItems(Arrays.asList(nonSuitableItem));
        specs.setMaxPkgWeight(BigDecimal.valueOf(40));
        return specs;
    }

    public static PackageSpecs getInvalidPackageSpecs() {
        Item invalidItem = new Item();
        invalidItem.setWeight(BigDecimal.valueOf(120));
        PackageSpecs specs = new PackageSpecs();
        specs.setItems(Arrays.asList(invalidItem));
        specs.setMaxPkgWeight(BigDecimal.valueOf(100));
        return specs;
    }

}
