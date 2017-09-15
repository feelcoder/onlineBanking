package com.kiffcode.banking.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the BillPayment entity. This class is used in BillPaymentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bill-payments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillPaymentCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter scheduledDate;

    private StringFilter frequencyMode;

    private BooleanFilter paymentComplete;

    private DoubleFilter amount;

    private LongFilter vendorId;

    private LongFilter accountUserId;

    public BillPaymentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(StringFilter scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public StringFilter getFrequencyMode() {
        return frequencyMode;
    }

    public void setFrequencyMode(StringFilter frequencyMode) {
        this.frequencyMode = frequencyMode;
    }

    public BooleanFilter getPaymentComplete() {
        return paymentComplete;
    }

    public void setPaymentComplete(BooleanFilter paymentComplete) {
        this.paymentComplete = paymentComplete;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public LongFilter getVendorId() {
        return vendorId;
    }

    public void setVendorId(LongFilter vendorId) {
        this.vendorId = vendorId;
    }

    public LongFilter getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(LongFilter accountUserId) {
        this.accountUserId = accountUserId;
    }

    @Override
    public String toString() {
        return "BillPaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (scheduledDate != null ? "scheduledDate=" + scheduledDate + ", " : "") +
                (frequencyMode != null ? "frequencyMode=" + frequencyMode + ", " : "") +
                (paymentComplete != null ? "paymentComplete=" + paymentComplete + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (vendorId != null ? "vendorId=" + vendorId + ", " : "") +
                (accountUserId != null ? "accountUserId=" + accountUserId + ", " : "") +
            "}";
    }

}
