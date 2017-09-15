package com.kiffcode.banking.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the BillPayment entity.
 */
public class BillPaymentDTO implements Serializable {

    private Long id;

    private String scheduledDate;

    private String frequencyMode;

    @NotNull
    private Boolean paymentComplete;

    @NotNull
    private Double amount;

    private Long vendorId;

    private Long accountUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getFrequencyMode() {
        return frequencyMode;
    }

    public void setFrequencyMode(String frequencyMode) {
        this.frequencyMode = frequencyMode;
    }

    public Boolean isPaymentComplete() {
        return paymentComplete;
    }

    public void setPaymentComplete(Boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(Long accountUserId) {
        this.accountUserId = accountUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillPaymentDTO billPaymentDTO = (BillPaymentDTO) o;
        if(billPaymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billPaymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillPaymentDTO{" +
            "id=" + getId() +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", frequencyMode='" + getFrequencyMode() + "'" +
            ", paymentComplete='" + isPaymentComplete() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
