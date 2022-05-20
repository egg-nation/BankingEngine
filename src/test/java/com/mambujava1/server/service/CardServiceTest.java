package com.mambujava1.server.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.Card;
import com.mambujava1.server.repository.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CardServiceTest {

    protected Injector injector = Guice.createInjector(new IoCModuleService());

    @Mock
    CardRepository cardRepository;
    @InjectMocks
    CardService cardService = injector.getInstance(CardService.class);

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
    }

    CardServiceTest() {

    }

    @Test
    void whenCreateCard() {
        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.insert("1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1)).thenReturn(card);

        //when
        boolean result = cardService.createCard("1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);

        //then
        assertTrue(result);
    }

    @Test
    void whenFreezeCardWithCorrectIdAndCorrectPin() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.freezeCard(1, "1545");

        //then
        assertTrue(result);
    }

    @Test
    void whenFreezeCardWithWrongId() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.freezeCard(2, "1545");

        //then
        assertFalse(result);
    }

    @Test
    void whenFreezeCardWithCorrectIdAndWrongPin() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.freezeCard(1, "3256");

        //then
        assertFalse(result);
    }

    @Test
    void whenUnfreezeCardWithCorrectIdAndCorrectPin() {

        //given
        Card card = new Card(6, "6384", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(6)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.unfreezeCard(6, "6384");

        //then
        assertTrue(result);
    }

    @Test
    void whenUnfreezeCardWithWrongId() {

        //given
        Card card = new Card(6, "6384", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(6)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.unfreezeCard(3, "6384");

        //then
        assertFalse(result);
    }

    @Test
    void whenUnfreezeCardWithCorrectIdAndWrongPin() {

        //given
        Card card = new Card(6, "6384", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(6)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.unfreezeCard(6, "2356");

        //then
        assertFalse(result);
    }

    @Test
    void whenRemoveCardWithCorrectIdAndCorrectPin() {

        //given
        Card card = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(2)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.removeCard(2, "5743");

        //then
        assertTrue(result);
    }

    @Test
    void whenRemoveCardWithWrongId() {

        //given
        Card card = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(2)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.removeCard(1, "5743");

        //then
        assertFalse(result);
    }

    @Test
    void whenRemoveCardWithCorrectIdAndWrongPin() {

        //given
        Card card = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(2)).thenReturn(Optional.of(card));

        //when
        boolean result = cardService.removeCard(1, "5742");

        //then
        assertFalse(result);
    }

    @Test
    void whenFindCardsByCorrectAccountId() {

        //given
        Card card1 = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        Card card2 = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        List<Card> expected = List.of(card1, card2);
        when(cardRepository.findByAccountId(1)).thenReturn(List.of(card1, card2));

        //when
        List<Card> returned = cardService.findCardsByAccountId(1);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void whenFindCardsByWrongAccountId() {

        //given
        Card card1 = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        Card card2 = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findByAccountId(1)).thenReturn(List.of(card1, card2));

        //when
        boolean returned = cardService.findCardsByAccountId(2).isEmpty();

        //then
        assertTrue(returned);
    }

    @Test
    void whenFindByCorrectId() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        Card returned = cardService.findById(1);

        //then
        assertEquals(card, returned);
    }

    @Test
    void whenFindByWrongId() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        Card returned = cardService.findById(2);

        //then
        assertNull(returned);
    }

    @Test
    void whenFindCardByCorrectIdAndCorrectPin() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        Card returned = cardService.findCardByIdAndPin(1, "1545");

        //then
        assertEquals(card, returned);
    }

    @Test
    void whenFindCardByWrongIdAndPin() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        Card returned = cardService.findCardByIdAndPin(2, "1545");

        //then
        assertNull(returned);
    }

    @Test
    void whenFindCardByCorrectIdAndWrongPin() {

        //given
        Card card = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        //when
        Card returned = cardService.findCardByIdAndPin(1, "1445");

        //then
        assertNull(returned);
    }

    @Test
    void whenViewCardAll() {

        //given
        Card card1 = new Card(2, "5743", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        Card card2 = new Card(1, "1545", LocalDate.of(2022, 5, 12), LocalDate.of(2026, 5, 31), 1, 1);
        List<Card> expected = List.of(card1, card2);
        when(cardRepository.getCards()).thenReturn(List.of(card1, card2));

        //when
        List<Card> returned = cardService.viewCardAll();

        //then
        assertEquals(expected, returned);
    }
}