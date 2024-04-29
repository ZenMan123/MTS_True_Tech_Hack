package org.example.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Wallet API",
                version = "1.0.0",
                description = "Wallet service",
                contact = @Contact(name = "RuslanHkmoff", url = "https://t.me/hkmoff")
        )
)
public class SwaggerConfiguration {
}