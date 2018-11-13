package io.service.money.model.dao;

import java.util.UUID;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class Account extends BaseModel<String> {

    public Account() {
        super(UUID.randomUUID().toString());
    }
}
