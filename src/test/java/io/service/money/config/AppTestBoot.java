package io.service.money.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.controller.routing.IRouting;

import java.util.Collections;
import java.util.Set;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
@Singleton
public class AppTestBoot {

    @Inject
    private Javalin javalin;

    @Inject(optional = true)
    private Set<IRouting> routes = Collections.emptySet();

    public void boot(String[] args) {
        routes.forEach(IRouting::handle);
        javalin.port(8080).start();
    }
}
