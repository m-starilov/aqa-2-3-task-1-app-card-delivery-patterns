package ru.netology.web.test;

import io.qameta.allure.selenide.AllureSelenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataForCardDeliveryOrder;
import ru.netology.web.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {
    private DataForCardDeliveryOrder clientInfo = DataGenerator.generateClientInfo();


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    void getValidCity(SelenideElement cityField) {
        while (!$(byText(clientInfo.getCity())).exists()) {
            clientInfo = DataGenerator.generateClientInfo();
            cityField.sendKeys(Keys.BACK_SPACE);
            cityField.sendKeys(Keys.BACK_SPACE);
            cityField.sendKeys(clientInfo.getCity().substring(0, 2));
        }
    }

    @Test
    void shouldRescheduledRequest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        SelenideElement cityField = form.$("[data-test-id=city] input");
        cityField.sendKeys(clientInfo.getCity().substring(0, 2));
        if (!$(byText(clientInfo.getCity())).exists()) getValidCity(cityField);
        $(byText(clientInfo.getCity())).click();
        SelenideElement dateField = form.$$("[data-test-id=date] input").last();
        dateField.doubleClick().sendKeys(Keys.BACK_SPACE);
        dateField.setValue(clientInfo.getDate());
        form.$("[data-test-id=name] input").setValue(clientInfo.getName());
        form.$("[data-test-id=phone] input").setValue(clientInfo.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible);
        $(withText("Встреча успешно запланирована")).shouldBe(visible);
        $(withText(clientInfo.getDate())).shouldBe(visible);

        open("http://localhost:9999");
        cityField.setValue(clientInfo.getCity());
        dateField.doubleClick().sendKeys(Keys.BACK_SPACE);
        dateField.setValue(clientInfo.getNewDate());
        form.$("[data-test-id=name] input").setValue(clientInfo.getName());
        form.$("[data-test-id=phone] input").setValue(clientInfo.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $(byText("Необходимо подтверждение")).shouldBe(visible);
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$("button").find(exactText("Перепланировать")).click();
        $(byText("Успешно!")).shouldBe(visible);
        $(withText("Встреча успешно запланирована")).shouldBe(visible);
        $(withText(clientInfo.getNewDate())).shouldBe(visible);
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
}