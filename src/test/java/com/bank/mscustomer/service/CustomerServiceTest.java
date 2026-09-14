package com.bank.mscustomer.service;

import com.bank.mscustomer.dto.CustomerResponse;
import com.bank.mscustomer.event.CustomerEventProducer;
import com.bank.mscustomer.model.BusinessCustomer;
import com.bank.mscustomer.model.Customer;
import com.bank.mscustomer.model.PersonalCustomer;
import com.bank.mscustomer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link CustomerService}.
 * Valida CRUD completo, validaciones de negocio y publicacion de eventos.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEventProducer customerEventProducer;

    @InjectMocks
    private CustomerService customerService;

    private PersonalCustomer personalCustomer;
    private BusinessCustomer businessCustomer;

    @BeforeEach
    void setUp() {
        personalCustomer = new PersonalCustomer("12345678", "Juan Perez", "REGULAR");
        personalCustomer.setId("id-1");

        businessCustomer = new BusinessCustomer("87654321", "Acme Corp", "REGULAR", "20123456789");
        businessCustomer.setId("id-2");
    }

    @Test
    void createPersonalCustomer_success() {
        when(customerRepository.existsByDocumentNumber("12345678")).thenReturn(false);
        when(customerRepository.save(any(PersonalCustomer.class))).thenReturn(personalCustomer);

        Customer result = customerService.createPersonalCustomer("12345678", "Juan Perez", "REGULAR");

        assertNotNull(result);
        assertEquals("12345678", result.getDocumentNumber());
        assertEquals("Juan Perez", result.getFullName());
        assertEquals("PERSONAL", result.getCustomerType());
        verify(customerEventProducer).publishCustomerCreated(personalCustomer);
    }

    @Test
    void createPersonalCustomer_duplicateDocument_throws() {
        when(customerRepository.existsByDocumentNumber("12345678")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> customerService.createPersonalCustomer("12345678", "Juan Perez", "REGULAR"));
        verify(customerRepository, never()).save(any());
        verify(customerEventProducer, never()).publishCustomerCreated(any());
    }

    @Test
    void createBusinessCustomer_success() {
        when(customerRepository.existsByDocumentNumber("87654321")).thenReturn(false);
        when(customerRepository.save(any(BusinessCustomer.class))).thenReturn(businessCustomer);

        Customer result = customerService.createBusinessCustomer("87654321", "Acme Corp", "REGULAR", "20123456789");

        assertNotNull(result);
        assertEquals("87654321", result.getDocumentNumber());
        assertEquals("BUSINESS", result.getCustomerType());
        verify(customerEventProducer).publishCustomerCreated(businessCustomer);
    }

    @Test
    void createBusinessCustomer_duplicateDocument_throws() {
        when(customerRepository.existsByDocumentNumber("87654321")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> customerService.createBusinessCustomer("87654321", "Acme Corp", "REGULAR", "20123456789"));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void findById_found() {
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(personalCustomer));

        Optional<Customer> result = customerService.findById("id-1");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void findById_notFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.findById("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_returnsList() {
        List<Customer> customers = Arrays.asList(personalCustomer, businessCustomer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void update_found_updatesFields() {
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(personalCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(personalCustomer);

        Optional<Customer> result = customerService.update("id-1", "Juan Perez Actualizado", "VIP");

        assertTrue(result.isPresent());
        assertEquals("Juan Perez Actualizado", result.get().getFullName());
        assertEquals("VIP", result.get().getProfile());
    }

    @Test
    void update_found_partialUpdate() {
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(personalCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(personalCustomer);

        Optional<Customer> result = customerService.update("id-1", null, "VIP");

        assertTrue(result.isPresent());
        assertEquals("VIP", result.get().getProfile());
        assertEquals("Juan Perez", result.get().getFullName());
    }

    @Test
    void update_notFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.update("nonexistent", "New Name", "VIP");

        assertFalse(result.isPresent());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void delete_found_returnsTrue() {
        when(customerRepository.existsById("id-1")).thenReturn(true);

        boolean result = customerService.delete("id-1");

        assertTrue(result);
        verify(customerRepository).deleteById("id-1");
    }

    @Test
    void delete_notFound_returnsFalse() {
        when(customerRepository.existsById("nonexistent")).thenReturn(false);

        boolean result = customerService.delete("nonexistent");

        assertFalse(result);
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void toResponse_personalCustomer() {
        CustomerResponse response = customerService.toResponse(personalCustomer);

        assertEquals("id-1", response.getId());
        assertEquals("12345678", response.getDocumentNumber());
        assertEquals("Juan Perez", response.getFullName());
        assertEquals("PERSONAL", response.getCustomerType());
        assertEquals("REGULAR", response.getProfile());
        assertNull(response.getRuc());
    }

    @Test
    void toResponse_businessCustomer() {
        CustomerResponse response = customerService.toResponse(businessCustomer);

        assertEquals("id-2", response.getId());
        assertEquals("BUSINESS", response.getCustomerType());
        assertEquals("20123456789", response.getRuc());
    }

    @Test
    void toResponseList() {
        List<Customer> customers = Arrays.asList(personalCustomer, businessCustomer);

        List<CustomerResponse> responses = customerService.toResponseList(customers);

        assertEquals(2, responses.size());
    }
}
