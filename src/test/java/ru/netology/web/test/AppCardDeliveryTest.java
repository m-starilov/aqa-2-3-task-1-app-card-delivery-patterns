package ru.netology.web.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataForCardDeliveryOrder;
import ru.netology.web.data.DataGenerator;

import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DataForCardDeliveryOrder clientInfo = DataGenerator.generateClientInfo();

    @Test
    void shouldRescheduledRequest() {
        open("http://localhost:9999");

        SelenideElement form = $("form");
        SelenideElement cityInput = form.$("[data-test-id=city] input");
        cityInput.sendKeys(clientInfo.getCity().substring(0,2));
        while (!$(byText(clientInfo.getCity())).exists()) {
            clientInfo = DataGenerator.generateClientInfo();
            cityInput.sendKeys(Keys.BACK_SPACE);
            cityInput.sendKeys(Keys.BACK_SPACE);
            cityInput.sendKeys(clientInfo.getCity().substring(0,2));
        }
        $(byText(clientInfo.getCity())).click();
        SelenideElement date = form.$$("[data-test-id=date] input").last();
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(clientInfo.getDate().format(formatter));
        form.$("[data-test-id=name] input").setValue(clientInfo.getName());
        form.$("[data-test-id=phone] input").setValue(clientInfo.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible);
        $(withText("Встреча успешно запланирована")).shouldBe(visible);
        $(withText(clientInfo.getDate().format(formatter))).shouldBe(visible);

        open("http://localhost:9999");
        cityInput.setValue(clientInfo.getCity());
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(clientInfo.getDate().plusDays(1).format(formatter));
        form.$("[data-test-id=name] input").setValue(clientInfo.getName());
        form.$("[data-test-id=phone] input").setValue(clientInfo.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $(byText("Необходимо подтверждение")).shouldBe(visible);
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$("button").find(exactText("Перепланировать")).click();
        $(byText("Успешно!")).shouldBe(visible);
        $(withText("Встреча успешно запланирована")).shouldBe(visible);
        $(withText(clientInfo.getDate().plusDays(1).format(formatter))).shouldBe(visible);
    }
}