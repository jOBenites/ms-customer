package com.bank.mscustomer.service;

import com.bank.mscustomer.dto.CustomerResponse;
import com.bank.mscustomer.event.CustomerEventProducer;
import com.bank.mscustomer.model.BusinessCustomer;
import com.bank.mscustomer.model.Customer;
import com.bank.mscustomer.model.PersonalCustomer;
import com.bank.mscustomer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de gestion de clientes.
 * Expone CRUD completo para clientes personales y empresariales,
 * y publica eventos de dominio al crear nuevos clientes.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventProducer customerEventProducer;

    /**
     * Crea un nuevo cliente personal.
     * Valida que no exista otro cliente con el mismo numero de documento.
     *
     * @param documentNumber numero de documento unico
     * @param fullName nombre completo
     * @param profile perfil del cliente (REGULAR o VIP)
     * @return el cliente creado
     * @throws IllegalArgumentException si ya existe un cliente con ese documento
     */
    public Customer createPersonalCustomer(String documentNumber, String fullName, String profile) {
        if (customerRepository.existsByDocumentNumber(documentNumber)) {
            throw new IllegalArgumentException("Ya existe un cliente con documento: " + documentNumber);
        }
        PersonalCustomer customer = new PersonalCustomer(documentNumber, fullName, profile);
        Customer saved = customerRepository.save(customer);
        customerEventProducer.publishCustomerCreated(saved);
        return saved;
    }

    /**
     * Crea un nuevo cliente empresarial.
     * Valida que no exista otro cliente con el mismo numero de documento.
     *
     * @param documentNumber numero de documento unico
     * @param fullName razon social
     * @param profile perfil del cliente (REGULAR o PYME)
     * @param ruc registro unico de contribuyente
     * @return el cliente creado
     * @throws IllegalArgumentException si ya existe un cliente con ese documento
     */
    public Customer createBusinessCustomer(String documentNumber, String fullName, String profile, String ruc) {
        if (customerRepository.existsByDocumentNumber(documentNumber)) {
            throw new IllegalArgumentException("Ya existe un cliente con documento: " + documentNumber);
        }
        BusinessCustomer customer = new BusinessCustomer(documentNumber, fullName, profile, ruc);
        Customer saved = customerRepository.save(customer);
        customerEventProducer.publishCustomerCreated(saved);
        return saved;
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return optional con el cliente encontrado
     */
    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    /**
     * Lista todos los clientes registrados.
     *
     * @return lista de clientes
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id identificador del cliente
     * @param fullName nuevo nombre completo (nullable, mantiene el actual)
     * @param profile nuevo perfil (nullable, mantiene el actual)
     * @return el cliente actualizado, o empty si no se encontro
     */
    public Optional<Customer> update(String id, String fullName, String profile) {
        return customerRepository.findById(id).map(customer -> {
            if (fullName != null) {
                customer.setFullName(fullName);
            }
            if (profile != null) {
                customer.setProfile(profile);
            }
            return customerRepository.save(customer);
        });
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return true si se elimino, false si no existia
     */
    public boolean delete(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convierte una entidad Customer a su DTO de respuesta.
     *
     * @param customer entidad a convertir
     * @return DTO con los campos poblados
     */
    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setDocumentNumber(customer.getDocumentNumber());
        response.setFullName(customer.getFullName());
        response.setCustomerType(customer.getCustomerType());
        response.setProfile(customer.getProfile());
        if (customer instanceof BusinessCustomer businessCustomer) {
            response.setRuc(businessCustomer.getRuc());
        }
        return response;
    }

    /**
     * Convierte una lista de entidades Customer a DTOs de respuesta.
     *
     * @param customers lista de entidades
     * @return lista de DTOs
     */
    public List<CustomerResponse> toResponseList(List<Customer> customers) {
        return customers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
