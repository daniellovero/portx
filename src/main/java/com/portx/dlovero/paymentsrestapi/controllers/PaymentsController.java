package com.portx.dlovero.paymentsrestapi.controllers;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.i18n.Messages;
import com.portx.dlovero.paymentsrestapi.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PaymentsController {

    public static final String IDEMPOTENT_KEY_HEADER = "idempotent-key";
    PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = Messages.Api.GET_PAYMENT_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.Api.GET_PAYMENT_200_DESCRIPTION,
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400", description = Messages.Api.GET_PAYMENT_400_DESCRIPTION,
                    content = @Content),
            @ApiResponse(responseCode = "404", description = Messages.Api.GET_PAYMENT_404_DESCRIPTION,
                    content = @Content)})
    @GetMapping(value = "/payments/{id}")
    public ResponseEntity getPaymentById(@Parameter(description = Messages.Api.GET_PAYMENT_ID_PARAMETER_DESCRIPTION) @PathVariable long id) {
        return paymentService.getPaymentById(id).map(payment -> new ResponseEntity(payment, HttpStatus.OK)).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = Messages.Api.POST_PAYMENT_OPERATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = Messages.Api.POST_PAYMENT_202_DESCRIPTION,
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400", description = Messages.Api.POST_PAYMENT_400_DESCRIPTION,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = Messages.Api.POST_PAYMENT_500_DESCRIPTION,
                    content = @Content)})
    @PostMapping(value = "/payments")
    public ResponseEntity postPayment(@Parameter(description = Messages.Api.POST_PAYMENT_BODY_DESCRIPTION) @RequestBody Payment payment, @Parameter(description = Messages.Api.POST_PAYMENT_IDEMPOTENT_KEY_DESCRIPTION) @RequestHeader(name = IDEMPOTENT_KEY_HEADER) UUID idempotentKey) {
        return paymentService.saveAndPublishPayment(payment, idempotentKey).map(savedPayment -> new ResponseEntity(savedPayment, HttpStatus.ACCEPTED)).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));
    }
}
