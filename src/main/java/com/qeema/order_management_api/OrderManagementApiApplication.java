package com.qeema.order_management_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef="auditAwareImpl")
@OpenAPIDefinition(
		info= @Info(
				title = "Qeema Order Management Api",
				description = "develop a rest-API that simulates order management fulfillment",
				version = "v1",
				contact = @Contact(
						name= "salma sherif",
						email = "salma.sherif.mohamed1@gmail.com"
				)

		)
)
public class OrderManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApiApplication.class, args);
	}

}
