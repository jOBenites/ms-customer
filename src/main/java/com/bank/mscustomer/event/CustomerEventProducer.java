package com.bank.mscustomer.event;

import com.bank.mscustomer.model.Customer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Productor de eventos Kafka para el dominio customer.
 * Publica bank.customer.created cuando se registra un nuevo cliente.
 */
@Component
@RequiredArgsConstructor
public class CustomerEventProducer {

    private static final Logger log = LoggerFactory.getLogger(CustomerEventProducer.class);
    private static final String CUSTOMER_CREATED_TOPIC = "bank.customer.created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publica el evento bank.customer.created con los datos minimos del cliente.
     *
     * @param customer recien creado
     */
    public void publishCustomerCreated(Customer customer) {
        Map<String, Object> payload = Map.of(
                "customerId", customer.getId(),
                "customerType", customer.getCustomerType(),
                "profile", customer.getProfile(),
                "documentNumber", customer.getDocumentNumber(),
                "occurredAt", LocalDateTime.now()
        );
        kafkaTemplate.send(CUSTOMER_CREATED_TOPIC, customer.getId(), payload);
        log.info("Evento bank.customer.created publicado para cliente {}", customer.getId());
    }
}
