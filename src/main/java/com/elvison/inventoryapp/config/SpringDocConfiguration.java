package com.elvison.inventoryapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI openApi(
            @Value("${app.version:0.0.0}")
                    String appVersion
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title("InventoryApp REST API")
                        .description("API specifications for InventoryApp")
                        .version(appVersion)
                );
    }
}
