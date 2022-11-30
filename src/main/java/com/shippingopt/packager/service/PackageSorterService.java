package com.shippingopt.packager.service;

import com.shippingopt.packager.dto.request.Item;
import com.shippingopt.packager.dto.request.ItemToRatio;
import com.shippingopt.packager.dto.request.PackageSpecs;
import com.shippingopt.packager.exception.NoSuitableItemException;
import com.shippingopt.packager.exception.PriceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
public class PackageSorterService {

    private static final String PRICE_ERROR_MSG = "Item price must be <= 100 %s. Item %d has a price of %.2f %s";

    public List<Long> getItemIdsFromPkgSpecs(PackageSpecs packageSpecs) throws NoSuitableItemException {
        log.info("Processing packageSpecs: ", packageSpecs);
        List<ItemToRatio> itemToRatioList;
        log.info("Filtering items exceeding max weight cap");
        List<Item> iterableItems = packageSpecs.getItems().stream()
                .filter(x -> x.getWeight().compareTo(packageSpecs.getMaxPkgWeight()) < 1)
                .toList();
        log.info("Sorting by price/weight ratio");
        if (!iterableItems.isEmpty()) {
            itemToRatioList = sortByPriceWeight(iterableItems);
        } else {
            throw new NoSuitableItemException("No provided item  matches the constraints");
        }
        log.info("Package optimization");
        List<Item> sortedItems = optimizePackage(packageSpecs.getMaxPkgWeight(),
                itemToRatioList.stream().map(ItemToRatio::getItem).toList());
        log.info("A total of " + sortedItems.size() + " items has been packed");
        return sortedItems.stream().map(Item::getId).toList();
    }

    // sort the list by price/weight raatio
    private List<ItemToRatio> sortByPriceWeight(List<Item> items) {
        return items.stream().map(x -> {
                    ItemToRatio itemEntry = new ItemToRatio();
                    itemEntry.setItem(x);
                    itemEntry.setPriceToWeightRatio(x.getPrice().divide(x.getWeight(), 2, RoundingMode.HALF_UP));
                    return itemEntry;
                })
                .sorted(Comparator.comparing(ItemToRatio::getPriceToWeightRatio).reversed()).toList();
    }

    // manual check for values exceeding 100 EUR (after possible conversion)
    public void validatePrice(PackageSpecs packageSpecs, String baseCurrency) throws PriceValidationException {
        for (int i = 0; i < packageSpecs.getItems().size(); i++) {
            Item iteratedItem = packageSpecs.getItems().get(i);
            if (iteratedItem.getPrice().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new PriceValidationException(
                        String.format(Locale.getDefault(), PRICE_ERROR_MSG, baseCurrency, i, iteratedItem.getPrice(), baseCurrency)
                );
            }
        }
    }

    private List<Item> optimizePackage(BigDecimal maxWeight, List<Item> sortedItems) {
        List<List<Item>> packageOpts = new ArrayList<>();
        for (int i = 0; i < sortedItems.size(); i++) {
            List<Item> packageCombo = getPackageCombination(i, sortedItems, maxWeight, packageOpts);
            if (packageCombo != null && !packageCombo.isEmpty()) {
                packageOpts.add(packageCombo);
            }
        }

        // compare first by total price and order DESC, then by weight ASC, combining the two
        Comparator<List<Item>> highestPriceLowestWeight = Comparator.comparing((List<Item> itemList) ->
            itemList.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
        ).reversed()
                .thenComparing((List<Item> itemList) ->
                itemList.stream().map(Item::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        Optional<List<Item>> bestPackageOpt = packageOpts.stream().sorted(highestPriceLowestWeight).findFirst();
        if (bestPackageOpt.isPresent()) {
            return bestPackageOpt.get();
        } else {
            // failsafe against regression, works for basic cases
            return packageOpts.get(0);
        }
    }

    // find all possible combinations with a package item
    public List<Item> getPackageCombination(Integer pivotIndex, List<Item> itemsList, BigDecimal maxWeight, List<List<Item>> previousCombos) {
        if (pivotIndex < itemsList.size()) {

            // select a pivot element
            Item pivotItem = itemsList.get(pivotIndex);
            LinkedList<Item> itemsToPack = new LinkedList<>();
            // this element will be added to the pack by default
            itemsToPack.add(pivotItem);

            // iterate the rest of the elements
            LinkedList<Item> listWithoutPivot = new LinkedList<>(itemsList);
            listWithoutPivot.remove(pivotItem);

            // try adding one element in the list (previously ordered by price/weight ratio), until the weight limit is reached
            BigDecimal totalWeight = pivotItem.getWeight();
            for (Item item: listWithoutPivot) {
                boolean previouslyAddedCombo = checkIfComboPreviouslyAdded(itemsToPack, previousCombos, item);
                if (!previouslyAddedCombo) {
                    BigDecimal tempSummedWeight = totalWeight.add(item.getWeight());
                    if (tempSummedWeight.compareTo(maxWeight) < 1) {
                        totalWeight = totalWeight.add(item.getWeight());
                        itemsToPack.add(item);
                    }
                } else {
                    return new ArrayList<>();
                }

            }
            return itemsToPack;
        } else {
            return new ArrayList<>();
        }
    }

    private boolean checkIfComboPreviouslyAdded(List<Item> itemsToPack, List<List<Item>> previousCombos, Item itemToAdd) {
        boolean previouslyAddedCombo = false;
        ArrayList<Long> currentlyPackedIds = new ArrayList<>(itemsToPack.stream().map(Item::getId).sorted().toList());
        currentlyPackedIds.add(itemToAdd.getId());
        for (List<Item> itemList: previousCombos) {
            List<Long> idsInPreviousCombo = itemList.stream().map(Item::getId).sorted().toList();
            if (currentlyPackedIds.equals(idsInPreviousCombo)) {
                previouslyAddedCombo = true;
            }
        }
        return previouslyAddedCombo;
    }

}
