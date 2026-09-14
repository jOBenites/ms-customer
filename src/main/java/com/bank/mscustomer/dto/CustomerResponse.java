package com.bank.mscustomer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta generico para cualquier tipo de cliente.
 * Contiene los campos comunes a clientes personales y empresariales.
 */
@Getter
@Setter
public class CustomerResponse {

    private String id;
    private String documentNumber;
    private String fullName;
    private String customerType;
    private String profile;
    private String ruc;
}
