package io.service.money.support.config;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.javalin.Javalin;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.ITransferStorage;
import io.service.money.storage.impl.AccountStorage;
import io.service.money.storage.impl.TransferStorage;
import io.service.money.support.HttpExecutor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class JavalinInjector extends Assert {

    private static Javalin javalin;

    protected static IAccountStorage accountStorage;
    protected static ITransferStorage transferStorage;

    protected static String SERVER_HOST = "http://localhost:8080";
    protected final HttpExecutor executor = new HttpExecutor();
    protected final Gson gson = new Gson();

    @BeforeClass
    public static void setup() {
        Injector injector = Guice.createInjector(new ServiceBindConfig());
        injector.getInstance(AppTestBoot.class).boot(new String[0]);
        javalin = injector.getInstance(Javalin.class);
        accountStorage = injector.getInstance(AccountStorage.class);
        transferStorage = injector.getInstance(TransferStorage.class);
    }

    @AfterClass
    public static void cleanup() {
        javalin.stop();
    }
}
