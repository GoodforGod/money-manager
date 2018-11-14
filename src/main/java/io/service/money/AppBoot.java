package io.service.money;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.controller.AccountController;
import io.service.money.controller.TransferController;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
@Singleton
public class AppBoot {

    @Inject private TransferController transferController;
    @Inject private AccountController accountController;
    @Inject private Javalin javalin;

    public void boot(String[] args) {
        transferController.handle();
        accountController.handle();
        javalin.port(8080).start();
    }
}
