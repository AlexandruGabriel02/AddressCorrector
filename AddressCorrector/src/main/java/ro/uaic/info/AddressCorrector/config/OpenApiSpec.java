package ro.uaic.info.AddressCorrector.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Address Corrector", version = "0.0.1-SNAPSHOT"),
        servers = {@Server(url = "http://localhost:8081")},
        tags = {
                @Tag(name = "Corrector"),
        })
public class OpenApiSpec {
}

