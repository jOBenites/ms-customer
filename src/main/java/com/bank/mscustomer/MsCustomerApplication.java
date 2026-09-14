package com.bank.mscustomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Microservicio de gestion de clientes del sistema bancario.
 * Expone CRUD completo para clientes personales y empresariales,
 * y publica el evento bank.customer.created al registrar un nuevo cliente.
 */
@EnableMongoAuditing
@SpringBootApplication
public class MsCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCustomerApplication.class, args);
    }
}
