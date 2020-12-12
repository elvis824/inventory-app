package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.rest.ProductRequest;
import com.google.inject.internal.util.ImmutableMap;
import org.apache.commons.compress.utils.Sets;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ProductRequestValidationHelper extends DefaultNameValidationHelper {
    private static final Map<String, Set<String>> CATEGORY_PRODUCT_WHITE_MAP = ImmutableMap.of(
            "clothes", Sets.newHashSet("dress", "shirt", "shoe", "skirt", "sock"),
            "food", Sets.newHashSet("bread", "cake", "macaroni", "pizza", "salad")
    );

    public void validate(@NonNull ProductRequest request, @NonNull Category category) {
        super.validate(request);
        validateProductCategory(request.getName(), category.getName());
    }

    private void validateProductCategory(String productName, String categoryName) {
        // hardcoding the logic here for demonstration purpose
        if (categoryName == null) {
            throw new IllegalArgumentException("Category name is invalid");
        }

        Set<String> set = CATEGORY_PRODUCT_WHITE_MAP.get(categoryName.toLowerCase());
        if (set != null && !set.contains(productName.toLowerCase())) {
            throw new IllegalArgumentException(String.format("The product \"%s\" should not be in category \"%s\"", productName, categoryName));
        }
    }
}
