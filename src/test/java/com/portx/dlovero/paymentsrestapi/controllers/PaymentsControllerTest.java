package com.portx.dlovero.paymentsrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidField;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidUUIDVersion;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomPaymentAlreadyExists;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import com.portx.dlovero.paymentsrestapi.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {
    @MockBean
    PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;
    private PaymentFactory paymentFactory;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        paymentFactory = new PaymentFactory();
    }

    @Test
    void testThatGetByIdReturnsOkAndThePaymentIfExists() throws Exception {
        Payment validPayment = paymentFactory.createValidPayment();

        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(validPayment));
        mockMvc.perform(get("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.getValidIdempotentKey()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testThatGetByIdReturnsClientErrorAndEmptyIfThePaymentDoesNotExists() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.getValidIdempotentKey()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void testThatPostReturnsClientErrorIfIdempotentKeyIsInvalidWhileCreatingPayment() throws Exception {
        Payment validPayment = paymentFactory.createValidPayment();

        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(validPayment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Required header 'idempotent-key' is not present.\",\"instance\":\"/payments\"}"));
    }

    @Test
    void testThatPostReturnsServerErrorIfAnUnexpectedErrorHappensWhileCreatingPayment() throws Exception {
        Payment validPayment = paymentFactory.createValidPayment();

        when(paymentService.saveAndPublishPayment(any(Payment.class), any(UUID.class))).thenThrow(new RuntimeException("DB Error"));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(validPayment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.getValidIdempotentKey()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("DB Error"));
    }

    @Test
    void testThatPostReturnsServerErrorIfPaymentAlreadyExistsAndDoesNotReturnAnything() throws Exception {
        Payment invalidPayment = paymentFactory.createInvalidPayment();

        when(paymentService.saveAndPublishPayment(any(Payment.class), any(UUID.class))).thenThrow(new CustomPaymentAlreadyExists("Already exists"));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(invalidPayment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.getValidIdempotentKey()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Already exists"));
    }

    @Test
    void testThatPostReturnsClientErrorIfDomainValidationFailsAndDoesNotReturnAnything() throws Exception {
        Payment invalidPayment = paymentFactory.createInvalidPayment();

        when(paymentService.saveAndPublishPayment(any(Payment.class), any(UUID.class))).thenThrow(new CustomInvalidField("Some validation error"));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(invalidPayment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.getValidIdempotentKey()))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Some validation error"));
    }

    @Test
    void testThatPostReturnsClientErrorIfInvalidUUIDAndDoesNotReturnAnything() throws Exception {
        Payment invalidPayment = paymentFactory.createInvalidPayment();

        when(paymentService.saveAndPublishPayment(any(Payment.class), any(UUID.class))).thenThrow(new CustomInvalidUUIDVersion("Invalid UUID"));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(invalidPayment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, paymentFactory.createInvalidIdempotentKey()))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Invalid UUID"));
    }

    @Test
    void testThatPostReturnsAcceptedAndReturnsAPaymentIfItDoesNotExists() throws Exception {
        Payment validPayment = paymentFactory.createValidPayment();
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        when(paymentService.saveAndPublishPayment(any(Payment.class), any(UUID.class))).thenReturn(Optional.of(validPayment));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(validPayment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(PaymentsController.IDEMPOTENT_KEY_HEADER, validIdempotentKey.toString()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
