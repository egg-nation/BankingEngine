package com.mambujava1.server.repository.repositories.utils;

import com.mambujava1.server.model.Card;
import com.mambujava1.server.repository.repositories.CardRepository;
import com.mambujava1.server.repository.session.DatabaseConnection;
import org.jdbi.v3.core.Jdbi;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {

    private final Jdbi jdbi;
    private List<Card> cards;

    public CardRepositoryImpl() {

        this.jdbi = DatabaseConnection.getInstance().getConnection();

        update();
    }

    @Override
    public void update() {

        cards = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM card")
                .mapToBean(Card.class)
                .list());
    }

    @Override
    public Card insert(String pin, LocalDate startDate, LocalDate endDate, int active, int accountId) {

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate;
        if (endDate != null) {
            sqlEndDate = Date.valueOf(endDate);
        } else {
            sqlEndDate = null;
        }

        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO card (pin, start_date, end_date, active, account_id) VALUES (:pin, :start_date, :end_date, :active, :account_id)")
                .bind("pin", pin)
                .bind("start_date", sqlStartDate)
                .bind("end_date", sqlEndDate)
                .bind("active", active)
                .bind("account_id", accountId)
                .execute()
        );

        update();

        return cards.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public void delete(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM card WHERE (id = :id)")
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public List<Card> getCards() {

        return cards;
    }

    @Override
    public void freeze(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("UPDATE card SET active = :active WHERE (id = :id)")
                .bind("active", 0)
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public void unfreeze(int id) {

        jdbi.withHandle(handle -> handle.createUpdate("UPDATE card SET active = :active WHERE (id = :id)")
                .bind("active", 1)
                .bind("id", id)
                .execute()
        );

        update();
    }

    @Override
    public Optional<Card> findById(int id) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM card WHERE (id = :id)")
                .bind("id", id)
                .mapToBean(Card.class)
                .list()
                .stream()
                .findFirst()
        );
    }

    @Override
    public List<Card> findByAccountId(int accountId) {

        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM card WHERE (account_id = :account_id)")
                .bind("account_id", accountId)
                .mapToBean(Card.class)
                .list()
        );
    }
}
