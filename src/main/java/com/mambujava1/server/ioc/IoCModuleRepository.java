package com.mambujava1.server.ioc;

import com.google.inject.AbstractModule;
import com.mambujava1.server.repository.repositories.*;
import com.mambujava1.server.repository.repositories.utils.*;

public class IoCModuleRepository extends AbstractModule {

    @Override
    protected void configure() {

        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class);
        bind(AccountTypeRepository.class).to(AccountTypeRepositoryImpl.class);
        bind(CardRepository.class).to(CardRepositoryImpl.class);
        bind(TransactionRepository.class).to(TransactionRepositoryImpl.class);
    }
}
