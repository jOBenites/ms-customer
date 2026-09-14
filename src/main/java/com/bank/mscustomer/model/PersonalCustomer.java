package com.bank.mscustomer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Cliente personal del banco.
 * Un cliente personal puede tener perfil REGULAR o VIP.
 * El perfil VIP requiere tarjeta de credito previa (validado al abrir cuenta).
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "customer")
public class PersonalCustomer extends Customer {

    /**
     * Constructor para crear un cliente personal.
     *
     * @param documentNumber numero de documento
     * @param fullName nombre completo
     * @param profile perfil del cliente (REGULAR o VIP)
     */
    public PersonalCustomer(String documentNumber, String fullName, String profile) {
        super(documentNumber, fullName, "PERSONAL", profile);
    }
}
