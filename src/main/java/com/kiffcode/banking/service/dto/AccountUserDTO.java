package com.kiffcode.banking.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the AccountUser entity.
 */
public class AccountUserDTO implements Serializable {

    private Long id;

    private String accountType;

    private String accountNo;

    private Long balance;

    private Long limit;

    private Boolean isActivated;

    private Instant createdDate;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Boolean isIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountUserDTO accountUserDTO = (AccountUserDTO) o;
        if(accountUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountUserDTO{" +
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
