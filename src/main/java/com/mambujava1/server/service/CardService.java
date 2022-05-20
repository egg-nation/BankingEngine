package com.mambujava1.server.service;

import com.mambujava1.server.model.Card;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    boolean createCard(String pin, LocalDate startDate, LocalDate endDate, int isActive, int accountId);

    boolean freezeCard(int id, String pin);

    boolean unfreezeCard(int id, String pin);

    boolean removeCard(int id, String pin);

    List<Card> findCardsByAccountId(int accountId);

    Card findById(int id);

    Card findCardByIdAndPin(int id, String pin);

    List<Card> viewCardAll();
}
