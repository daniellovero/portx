package com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.entities;

import com.portx.dlovero.paymentsrestapi.domain.model.*;
import com.portx.dlovero.paymentsrestapi.repositories.DatabaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity implements DatabaseEntity<Payment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID idempotentKey;
    private BigDecimal amount;
    private String currency;
    private String originatorName;
    private Long originatorId;
    private String beneficiaryName;
    private Long beneficiaryId;
    private String senderAccountType;
    private Long senderAccountNumber;
    private String receiverAccountType;
    private Long receiverAccountNumber;
    private String paymentStatus;

    public PaymentEntity() {
    }

    public PaymentEntity(Payment payment, UUID idempotentKey) {
        this.idempotentKey = idempotentKey;
        this.amount = payment.getAmountPaid().getAmount();
        this.beneficiaryId = payment.getBeneficiary().getId();
        this.beneficiaryName = payment.getBeneficiary().getName();
        this.originatorId = payment.getOriginator().getId();
        this.originatorName = payment.getOriginator().getName();
        this.receiverAccountNumber = payment.getReceiver().getAccountNumber();
        this.receiverAccountType = payment.getReceiver().getAccountType();
        this.senderAccountNumber = payment.getSender().getAccountNumber();
        this.senderAccountType = payment.getSender().getAccountType();
        this.currency = payment.getAmountPaid().getCurrency();
        this.paymentStatus = "CREATED";
    }

    @Override
    public Payment toModel() {
        return new Payment(id, new AmountPaid(amount, currency), new Customer(originatorId, originatorName), new Customer(beneficiaryId, beneficiaryName), new Account(senderAccountNumber, senderAccountType), new Account(receiverAccountNumber, receiverAccountType), PAYMENT_STATUS.valueOf(paymentStatus));
    }

    public UUID getIdempotentKey() {
        return idempotentKey;
    }

    public void setIdempotentKey(UUID idempotentKey) {
        this.idempotentKey = idempotentKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public Long getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(Long originatorId) {
        this.originatorId = originatorId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getSenderAccountType() {
        return senderAccountType;
    }

    public void setSenderAccountType(String senderAccountType) {
        this.senderAccountType = senderAccountType;
    }

    public Long getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(Long senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountType() {
        return receiverAccountType;
    }

    public void setReceiverAccountType(String receiverAccountType) {
        this.receiverAccountType = receiverAccountType;
    }

    public Long getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(Long receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
