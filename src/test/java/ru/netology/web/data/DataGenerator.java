package ru.netology.web.data;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataGenerator {
    public DataGenerator() {
    }

    public static DataForCardDeliveryOrder generateClientInfo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Faker faker = new Faker(new Locale("ru"));
        return new DataForCardDeliveryOrder(
                faker.address().city(),
                simpleDateFormat.format(faker.date().future(7, 3, TimeUnit.DAYS)),
                simpleDateFormat.format(faker.date().future(10, 8, TimeUnit.DAYS)),
                String.join(" ", faker.name().lastName(), faker.name().firstName()),
                faker.phoneNumber().phoneNumber());
    }
}
