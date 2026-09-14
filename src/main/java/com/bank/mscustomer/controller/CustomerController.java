package com.bank.mscustomer.controller;

import com.bank.mscustomer.dto.CustomerResponse;
import com.bank.mscustomer.model.Customer;
import com.bank.mscustomer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestion de clientes.
 * Expone CRUD completo para clientes personales y empresariales.
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Crea un nuevo cliente personal.
     *
     * @param request campos del cliente personal (documentNumber, fullName, profile)
     * @return el cliente creado con codigo 201
     */
    @PostMapping("/personal")
    public ResponseEntity<CustomerResponse> createPersonalCustomer(
            @RequestBody Map<String, String> request) {
        Customer customer = customerService.createPersonalCustomer(
                request.get("documentNumber"),
                request.get("fullName"),
                request.get("profile")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.toResponse(customer));
    }

    /**
     * Crea un nuevo cliente empresarial.
     *
     * @param request campos del cliente empresarial (documentNumber, fullName, profile, ruc)
     * @return el cliente creado con codigo 201
     */
    @PostMapping("/business")
    public ResponseEntity<CustomerResponse> createBusinessCustomer(
            @RequestBody Map<String, String> request) {
        Customer customer = customerService.createBusinessCustomer(
                request.get("documentNumber"),
                request.get("fullName"),
                request.get("profile"),
                request.get("ruc")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.toResponse(customer));
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return el cliente encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(customerService.toResponse(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos los clientes registrados.
     *
     * @return lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.toResponseList(customerService.findAll());
        return ResponseEntity.ok(customers);
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id identificador del cliente
     * @param request campos a actualizar (fullName, profile)
     * @return el cliente actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {
        return customerService.update(id, request.get("fullName"), request.get("profile"))
                .map(customer -> ResponseEntity.ok(customerService.toResponse(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return 204 si se elimino, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        if (customerService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
