package com.mambujava1.server.model;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private int id;
    private String name;
    private String description;
    private float amount;
    private LocalDate date;
    private int accountId;

    public Transaction() {

    }

    public Transaction(int id, String name, String description, float amount, LocalDate date, int accountId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.accountId = accountId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public float getAmount() {

        return amount;
    }

    public void setAmount(float amount) {

        this.amount = amount;
    }

    public LocalDate getDate() {

        return date;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public int getAccountId() {

        return accountId;
    }

    public void setAccountId(int accountId) {

        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getId() == that.getId() && Float.compare(that.getAmount(), getAmount()) == 0 && getAccountId() == that.getAccountId() && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getDescription(), getAmount(), getDate(), getAccountId());
    }

    @Override
    public String toString() {

        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", accountId=" + accountId +
                "}\n";
    }
}
