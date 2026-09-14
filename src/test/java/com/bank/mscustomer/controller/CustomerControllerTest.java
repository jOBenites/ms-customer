package com.bank.mscustomer.controller;

import com.bank.mscustomer.dto.CustomerResponse;
import com.bank.mscustomer.model.Customer;
import com.bank.mscustomer.model.PersonalCustomer;
import com.bank.mscustomer.model.BusinessCustomer;
import com.bank.mscustomer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link CustomerController}.
 * Valida los endpoints REST y los codigos de respuesta HTTP.
 */
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private PersonalCustomer personalCustomer;
    private BusinessCustomer businessCustomer;
    private CustomerResponse personalResponse;
    private CustomerResponse businessResponse;

    @BeforeEach
    void setUp() {
        personalCustomer = new PersonalCustomer("12345678", "Juan Perez", "REGULAR");
        personalCustomer.setId("id-1");

        businessCustomer = new BusinessCustomer("87654321", "Acme Corp", "REGULAR", "20123456789");
        businessCustomer.setId("id-2");

        personalResponse = new CustomerResponse();
        personalResponse.setId("id-1");
        personalResponse.setDocumentNumber("12345678");
        personalResponse.setFullName("Juan Perez");
        personalResponse.setCustomerType("PERSONAL");
        personalResponse.setProfile("REGULAR");

        businessResponse = new CustomerResponse();
        businessResponse.setId("id-2");
        businessResponse.setDocumentNumber("87654321");
        businessResponse.setFullName("Acme Corp");
        businessResponse.setCustomerType("BUSINESS");
        businessResponse.setProfile("REGULAR");
        businessResponse.setRuc("20123456789");
    }

    @Test
    void createPersonalCustomer_returns201() {
        when(customerService.createPersonalCustomer("12345678", "Juan Perez", "REGULAR"))
                .thenReturn(personalCustomer);
        when(customerService.toResponse(personalCustomer)).thenReturn(personalResponse);

        Map<String, String> request = Map.of(
                "documentNumber", "12345678",
                "fullName", "Juan Perez",
                "profile", "REGULAR"
        );
        ResponseEntity<CustomerResponse> response = customerController.createPersonalCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("id-1", response.getBody().getId());
    }

    @Test
    void createBusinessCustomer_returns201() {
        when(customerService.createBusinessCustomer("87654321", "Acme Corp", "REGULAR", "20123456789"))
                .thenReturn(businessCustomer);
        when(customerService.toResponse(businessCustomer)).thenReturn(businessResponse);

        Map<String, String> request = Map.of(
                "documentNumber", "87654321",
                "fullName", "Acme Corp",
                "profile", "REGULAR",
                "ruc", "20123456789"
        );
        ResponseEntity<CustomerResponse> response = customerController.createBusinessCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("BUSINESS", response.getBody().getCustomerType());
    }

    @Test
    void getCustomerById_found() {
        when(customerService.findById("id-1")).thenReturn(Optional.of(personalCustomer));
        when(customerService.toResponse(personalCustomer)).thenReturn(personalResponse);

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById("id-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("id-1", response.getBody().getId());
    }

    @Test
    void getCustomerById_notFound() {
        when(customerService.findById("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllCustomers_returnsList() {
        List<CustomerResponse> responses = Arrays.asList(personalResponse, businessResponse);
        when(customerService.findAll()).thenReturn(Arrays.asList(personalCustomer, businessCustomer));
        when(customerService.toResponseList(Arrays.asList(personalCustomer, businessCustomer)))
                .thenReturn(responses);

        ResponseEntity<List<CustomerResponse>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateCustomer_found() {
        when(customerService.update("id-1", "Juan Nuevo", "VIP"))
                .thenReturn(Optional.of(personalCustomer));
        when(customerService.toResponse(personalCustomer)).thenReturn(personalResponse);

        Map<String, String> request = Map.of("fullName", "Juan Nuevo", "profile", "VIP");
        ResponseEntity<CustomerResponse> response = customerController.updateCustomer("id-1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateCustomer_notFound() {
        when(customerService.update("nonexistent", "Name", "VIP")).thenReturn(Optional.empty());

        Map<String, String> request = Map.of("fullName", "Name", "profile", "VIP");
        ResponseEntity<CustomerResponse> response = customerController.updateCustomer("nonexistent", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCustomer_found() {
        when(customerService.delete("id-1")).thenReturn(true);

        ResponseEntity<Void> response = customerController.deleteCustomer("id-1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteCustomer_notFound() {
        when(customerService.delete("nonexistent")).thenReturn(false);

        ResponseEntity<Void> response = customerController.deleteCustomer("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
