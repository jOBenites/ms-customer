package com.bank.mscustomer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Cliente empresarial del banco.
 * Un cliente empresarial puede tener perfil REGULAR o PYME.
 * El perfil PYME no paga comision de mantenimiento en cuenta corriente.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "customer")
public class BusinessCustomer extends Customer {

    private String ruc;

    /**
     * Constructor para crear un cliente empresarial.
     *
     * @param documentNumber numero de documento
     * @param fullName nombre completo o razon social
     * @param profile perfil del cliente (REGULAR o PYME)
     * @param ruc registro unico de contribuyente
     */
    public BusinessCustomer(String documentNumber, String fullName, String profile, String ruc) {
        super(documentNumber, fullName, "BUSINESS", profile);
        this.ruc = ruc;
    }
}
