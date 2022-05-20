package com.mambujava1.server.model;

import java.time.LocalDate;
import java.util.Objects;

public class Card {

    private int id;
    private String pin;
    private LocalDate startDate;
    private LocalDate endDate;
    private int active;
    private int accountId;

    public Card() {

    }

    public Card(int id, String pin, LocalDate startDate, LocalDate endDate, int active, int accountId) {

        this.id = id;
        this.pin = pin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.accountId = accountId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getPin() {

        return pin;
    }

    public void setPin(String pin) {

        this.pin = pin;
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

    public int getActive() {

        return active;
    }

    public void setActive(int active) {

        this.active = active;
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
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getId() == card.getId() && getActive() == card.getActive() && getAccountId() == card.getAccountId() && getPin().equals(card.getPin()) && getStartDate().equals(card.getStartDate()) && getEndDate().equals(card.getEndDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getPin(), getStartDate(), getEndDate(), getActive(), getAccountId());
    }

    @Override
    public String toString() {

        return "Card{" +
                "id=" + id +
                ", pin='" + pin + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", active=" + active +
                ", accountId=" + accountId +
                '}';
    }
}
