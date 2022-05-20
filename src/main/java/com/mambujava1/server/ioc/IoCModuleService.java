package com.mambujava1.server.ioc;

import com.google.inject.AbstractModule;
import com.mambujava1.server.service.*;
import com.mambujava1.server.service.utils.*;

public class IoCModuleService extends AbstractModule {

    @Override
    protected void configure() {

        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(CardService.class).to(CardServiceImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }
}
