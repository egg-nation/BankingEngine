package com.mambujava1.server.repository.repositories;

import com.mambujava1.server.model.Card;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends Repository {

    Card insert(String pin, LocalDate startDate, LocalDate endDate, int active, int accountId);

    void delete(int id);

    List<Card> getCards();

    void freeze(int id);

    void unfreeze(int id);

    Optional<Card> findById(int id);

    List<Card> findByAccountId(int accountId);
}
