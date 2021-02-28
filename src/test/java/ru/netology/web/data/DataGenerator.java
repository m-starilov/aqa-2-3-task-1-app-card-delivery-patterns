package ru.netology.web.data;

import com.github.javafaker.Faker;

import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataGenerator {
    public DataGenerator() {
    }

    public static DataForCardDeliveryOrder generateClientInfo() {
        Faker faker = new Faker(new Locale("ru"));
        return new DataForCardDeliveryOrder(
                faker.address().city(),
                faker.date().future(40, 3, TimeUnit.DAYS)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                String.join(" ", faker.name().lastName(), faker.name().firstName()),
                faker.phoneNumber().phoneNumber());
    }
}
