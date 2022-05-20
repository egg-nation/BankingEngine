package com.mambujava1.server.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mambujava1.server.ioc.IoCModuleService;
import com.mambujava1.server.model.User;
import com.mambujava1.server.repository.repositories.AccountRepository;
import com.mambujava1.server.repository.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    protected Injector injector = Guice.createInjector(new IoCModuleService());

    @Mock
    UserRepository userRepository;
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    UserService userService = injector.getInstance(UserService.class);

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
    }

    UserServiceTest() {

    }

    @Test
    void whenCreateUserWithNonexistentUsernameAndNonexistentEmailAddress() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.insert(any(), any(), any(), any(), any(), any())).thenReturn(user);

        // when
        User returnedUser = userService.createUser("Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");

        // then
        assertSame(user, returnedUser);
    }

    @Test
    void whenCreateUserWithExistentUsername() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.createUser("Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");

        // then
        assertNull(returnedUser);
    }

    @Test
    void whenCreateUserWithExistentUsernameAndExistentEmailAddress() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByEmailAddress(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.createUser("Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");

        // then
        assertNull(returnedUser);
    }

    @Test
    void whenLoginWithExistentUsernameAndCorrectPassword() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.login("andrew_scott", "passwordhash");

        // then
        assertSame(user, returnedUser);
    }

    @Test
    void whenLoginWithExistentEmailAddressAndCorrectPassword() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByEmailAddress(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.login("andrewscott@gmail.com", "passwordhash");

        // then
        assertSame(user, returnedUser);
    }

    @Test
    void whenLoginWithExistentUsernameAndIncorrectPassword() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.login("andrew_scott", "passwordhash1"
        );

        // then
        assertNull(returnedUser);
    }

    @Test
    void whenLoginWithExistentEmailAddressAndIncorrectPassword() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findByEmailAddress(any())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.login("andrewscott@gmail.com", "passwordhash1");

        // then
        assertNull(returnedUser);
    }

    @Test
    void whenLoginWithNonxistentUsernameOrEmailAddress() {

        // when
        User returnedUser = userService.login("andrewscott@gmail.com", "passwordhash");

        // then
        assertNull(returnedUser);
    }

    @Test
    void whenDeleteUser() {

        // given
        userRepository.insert("Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        accountRepository.insert(12, LocalDate.now(), null, 1, 1);

        // when
        userService.deleteUser(1);

        // then
        assertNull(userRepository.findById(1).orElse(null));
        assertNull(accountRepository.findById(1).orElse(null));
    }

    @Test
    void whenFindByCorrectId() {

        // given
        User user = new User(1, "Andrew", "Scott", "Iasi, Romania", "andrew_scott", "andrewscott@gmail.com", "passwordhash");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.findById(1);

        // then
        assertSame(user, returnedUser);
    }

    @Test
    void whenFindByIncorrectId() {

       // when
        User returnedUser = userService.findById(1);

        // then
        assertNull(returnedUser);
    }
}