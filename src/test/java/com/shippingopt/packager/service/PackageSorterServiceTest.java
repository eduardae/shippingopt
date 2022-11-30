package com.shippingopt.packager.service;

import com.shippingopt.packager.TestUtils;
import com.shippingopt.packager.dto.request.Item;
import com.shippingopt.packager.dto.request.PackageSpecs;
import com.shippingopt.packager.exception.NoSuitableItemException;
import com.shippingopt.packager.exception.PriceValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PackageSorterServiceTest {

    @Autowired
    private PackageSorterService packageSorterService;

    @Test
    void noSuitableItemsTest() {
        assertThrows(NoSuitableItemException.class, () -> packageSorterService.getItemIdsFromPkgSpecs(TestUtils.getNoSuitablePackageSpecs()));
    }

    @Test
    void maxPriceValidationException() {
        Item item1 = new Item();
        item1.setPrice(BigDecimal.valueOf(150));
        item1.setWeight(BigDecimal.valueOf(50));
        PackageSpecs specs = new PackageSpecs();
        specs.setItems(Arrays.asList(item1));
        specs.setMaxPkgWeight(BigDecimal.valueOf(60));
        specs.setCurrency("USD");
        assertThrows(PriceValidationException.class, () -> packageSorterService.validatePrice(specs, "EUR"));
    }

    @Test
    void packageOptimizationTest_2ItemsRes() throws NoSuitableItemException {
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
        specs.setCurrency("USD");
        List<Long> ids = packageSorterService.getItemIdsFromPkgSpecs(specs);
        Assertions.assertEquals(2, ids.size());
    }

    @Test
    void packageOptimizationTest_3ItemsResFringeCase() throws NoSuitableItemException {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setPrice(BigDecimal.valueOf(55));
        item1.setWeight(BigDecimal.valueOf(30));
        Item item2 = new Item();
        item2.setId(2L);
        item2.setPrice(BigDecimal.valueOf(79));
        item2.setWeight(BigDecimal.valueOf(56));
        Item item3 = new Item();
        item3.setId(3L);
        item3.setPrice(BigDecimal.valueOf(50));
        item3.setWeight(BigDecimal.valueOf(44));
        Item item4 = new Item();
        item4.setId(4L);
        item4.setPrice(BigDecimal.valueOf(30));
        item4.setWeight(BigDecimal.valueOf(25));
        PackageSpecs specs = new PackageSpecs();
        specs.setItems(Arrays.asList(item1, item2, item3, item4));
        specs.setMaxPkgWeight(BigDecimal.valueOf(100));
        specs.setCurrency("USD");
        List<Long> ids = packageSorterService.getItemIdsFromPkgSpecs(specs);
        Assertions.assertEquals(3, ids.size());
    }

}
