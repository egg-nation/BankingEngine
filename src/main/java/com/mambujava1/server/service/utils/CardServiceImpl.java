package com.mambujava1.server.service.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleRepository;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.repository.repositories.CardRepository;
import com.mambujava1.server.service.CardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CardServiceImpl implements CardService {

    protected CardRepository cardRepository;

    public CardServiceImpl() {

        Injector injector = Guice.createInjector(new IoCModuleRepository());
        this.cardRepository = injector.getInstance(CardRepository.class);
    }

    @Override
    public boolean createCard(String pin, LocalDate startDate, LocalDate endDate, int isActive, int accountId) {

        return Objects.nonNull(cardRepository.insert(pin, startDate, endDate, isActive, accountId));
    }

    @Override
    public boolean freezeCard(int id, String pin) {

        Card foundCard = findCardByIdAndPin(id, pin);

        if (Objects.nonNull(foundCard)) {
            cardRepository.freeze(id);
            return true;
        }

        return false;
    }

    @Override
    public boolean unfreezeCard(int id, String pin) {

        Card foundCard = findCardByIdAndPin(id, pin);

        if (Objects.nonNull(foundCard)) {
            cardRepository.unfreeze(id);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeCard(int id, String pin) {

        Card foundCard = findCardByIdAndPin(id, pin);

        if (Objects.nonNull(foundCard)) {
            cardRepository.delete(id);
            return true;
        }

        return false;
    }

    @Override
    public List<Card> findCardsByAccountId(int accountId) {

        return cardRepository.findByAccountId(accountId);
    }

    @Override
    public Card findById(int id) {

        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findCardByIdAndPin(int id, String pin) {

        Card foundCard = findById(id);

        if (Objects.nonNull(foundCard)) {
            if (foundCard.getPin().equals(pin)) {
                return foundCard;
            }
        }

        return null;
    }

    @Override
    public List<Card> viewCardAll() {

        return cardRepository.getCards();
    }
}
