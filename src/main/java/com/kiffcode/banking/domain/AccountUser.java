package com.kiffcode.banking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A AccountUser.
 */
@Entity
@Table(name = "account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "jhi_limit")
    private Long limit;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public AccountUser accountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public AccountUser accountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Long getBalance() {
        return balance;
    }

    public AccountUser balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLimit() {
        return limit;
    }

    public AccountUser limit(Long limit) {
        this.limit = limit;
        return this;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Boolean isIsActivated() {
        return isActivated;
    }

    public AccountUser isActivated(Boolean isActivated) {
        this.isActivated = isActivated;
        return this;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public AccountUser createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public AccountUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        AccountUser accountUser = (AccountUser) o;
        if (accountUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountUser{" +
            "id=" + getId() +
            ", accountType='" + getAccountType() + "'" +
            ", accountNo='" + getAccountNo() + "'" +
            ", balance='" + getBalance() + "'" +
            ", limit='" + getLimit() + "'" +
            ", isActivated='" + isIsActivated() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
