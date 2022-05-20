package com.mambujava1.server.model;

import java.util.Objects;

public class AccountType {

    private int id;
    private String name;
    private Float interestRate;

    public AccountType() {

    }

    public AccountType(int id, String name, Float interestRate) {

        this.id = id;
        this.name = name;
        this.interestRate = interestRate;
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

    public Float getInterestRate() {

        return interestRate;
    }

    public void setInterestRate(Float interestRate) {

        this.interestRate = interestRate;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof AccountType)) return false;
        AccountType that = (AccountType) o;
        return getId() == that.getId() && getName().equals(that.getName()) && Objects.equals(getInterestRate(), that.getInterestRate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getInterestRate());
    }

    @Override
    public String toString() {

        return "AccountType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", interestRate=" + interestRate +
                "}\n";
    }
}
