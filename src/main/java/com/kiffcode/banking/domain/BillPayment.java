package com.kiffcode.banking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BillPayment.
 */
@Entity
@Table(name = "bill_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BillPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduled_date")
    private String scheduledDate;

    @Column(name = "frequency_mode")
    private String frequencyMode;

    @NotNull
    @Column(name = "payment_complete", nullable = false)
    private Boolean paymentComplete;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne(optional = false)
    @NotNull
    private Vendor vendor;

    @ManyToOne(optional = false)
    @NotNull
    private AccountUser accountUser;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public BillPayment scheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getFrequencyMode() {
        return frequencyMode;
    }

    public BillPayment frequencyMode(String frequencyMode) {
        this.frequencyMode = frequencyMode;
        return this;
    }

    public void setFrequencyMode(String frequencyMode) {
        this.frequencyMode = frequencyMode;
    }

    public Boolean isPaymentComplete() {
        return paymentComplete;
    }

    public BillPayment paymentComplete(Boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
        return this;
    }

    public void setPaymentComplete(Boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
    }

    public Double getAmount() {
        return amount;
    }

    public BillPayment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public BillPayment vendor(Vendor vendor) {
        this.vendor = vendor;
        return this;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public AccountUser getAccountUser() {
        return accountUser;
    }

    public BillPayment accountUser(AccountUser accountUser) {
        this.accountUser = accountUser;
        return this;
    }

    public void setAccountUser(AccountUser accountUser) {
        this.accountUser = accountUser;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BillPayment billPayment = (BillPayment) o;
        if (billPayment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billPayment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillPayment{" +
            "id=" + getId() +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", frequencyMode='" + getFrequencyMode() + "'" +
            ", paymentComplete='" + isPaymentComplete() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
