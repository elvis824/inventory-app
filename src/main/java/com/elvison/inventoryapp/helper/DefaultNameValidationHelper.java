package com.elvison.inventoryapp.helper;

import com.elvison.inventoryapp.model.rest.NameRequest;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Primary
@Component
public class DefaultNameValidationHelper {

    private static final Pattern NAME_PATTERN = Pattern.compile("[^a-z0-9-_ ]", Pattern.CASE_INSENSITIVE);

    public void validate(@NonNull NameRequest request) {
        if (StringUtils.isEmpty(request.getName())) {
            throw new IllegalArgumentException("Name is empty");
        }
        if (NAME_PATTERN.matcher(request.getName()).find()) {
            throw new IllegalArgumentException("Some characters in given name is invalid");
        }
    }

}
