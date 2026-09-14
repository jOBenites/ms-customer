package com.bank.mscustomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de gestion de clientes del sistema bancario.
 * Expone CRUD completo para clientes personales y empresariales,
 * y publica el evento bank.customer.created al registrar un nuevo cliente.
 */
@SpringBootApplication
public class MsCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCustomerApplication.class, args);
    }
}
