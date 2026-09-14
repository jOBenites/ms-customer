package com.bank.mscustomer.repository;

import com.bank.mscustomer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad Customer en MongoDB.
 * Proporciona operaciones CRUD basicas y busqueda por numero de documento.
 * No se permite @Query ni consultas dinamicas segun las reglas del proyecto.
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

    /**
     * Verifica si existe un cliente con el numero de documento dado.
     *
     * @param documentNumber numero de documento a buscar
     * @return true si existe, false de lo contrario
     */
    boolean existsByDocumentNumber(String documentNumber);

    /**
     * Busca un cliente por su numero de documento.
     *
     * @param documentNumber numero de documento
     * @return optional con el cliente encontrado
     */
    Optional<Customer> findByDocumentNumber(String documentNumber);
}
