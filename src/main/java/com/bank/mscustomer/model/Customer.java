package com.bank.mscustomer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad base que representa un cliente del banco.
 * Cada cliente tiene un numero de documento unico y un nombre completo.
 * Las subclases {@link PersonalCustomer} y {@link BusinessCustomer}
 * agregan atributos especificos de cada tipo.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "customer")
public abstract class Customer {

    @Id
    private String id;

    @Indexed(unique = true)
    private String documentNumber;

    private String fullName;

    private String customerType;

    private String profile;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Constructor completo para la creacion de un cliente.
     *
     * @param documentNumber numero de documento unico
     * @param fullName nombre completo del cliente
     * @param customerType tipo de cliente (PERSONAL o BUSINESS)
     * @param profile perfil del cliente (REGULAR, VIP o PYME)
     */
    protected Customer(String documentNumber, String fullName, String customerType, String profile) {
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.customerType = customerType;
        this.profile = profile;
    }
}
