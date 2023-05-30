package crohnsassistantapi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Crohn´s Assistant API",
                description = "API for the Application 'Crohn´s Assistant",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alberto Ruibal Ojea",
                        email = "alberto.ruibal.ojea@outlook.es"
                ),
                license = @License(
                        name = "GPL-3.0 Licence",
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html")),
        servers = {
                @Server(url = "/", description = "General use server"),
                @Server(url = "testing", description = "Testing server")
        }
)
@SecurityScheme(
        name = "JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
public class OpenAPIConfiguration {
}