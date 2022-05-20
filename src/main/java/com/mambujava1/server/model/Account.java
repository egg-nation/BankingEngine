package com.mambujava1.server.model;

import java.time.LocalDate;
import java.util.Objects;

public class Account {

    private int id;
    private float amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int userId;
    private int accountTypeId;

    public Account() {

    }

    public Account(int id, float amount, LocalDate startDate, LocalDate endDate, int userId, int accountTypeId) {

        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.accountTypeId = accountTypeId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public float getAmount() {

        return amount;
    }

    public void setAmount(float amount) {

        this.amount = amount;
    }

    public LocalDate getStartDate() {

        return startDate;
    }

    public void setStartDate(LocalDate startDate) {

        this.startDate = startDate;
    }

    public LocalDate getEndDate() {

        return endDate;
    }

    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public int getAccountTypeId() {

        return accountTypeId;
    }

    public void setAccountTypeId(int accountTypeId) {

        this.accountTypeId = accountTypeId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId() && Float.compare(account.getAmount(), getAmount()) == 0 && getUserId() == account.getUserId() && getAccountTypeId() == account.getAccountTypeId() && getStartDate().equals(account.getStartDate()) && Objects.equals(getEndDate(), account.getEndDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getAmount(), getStartDate(), getEndDate(), getUserId(), getAccountTypeId());
    }

    @Override
    public String toString() {

        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userId=" + userId +
                ", accountTypeId=" + accountTypeId +
                "}\n";
    }
}
