package io.service.money;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.service.money.config.BindConfig;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class AppStarter {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BindConfig());
        injector.getInstance(AppBoot.class).boot(args);
    }
}
