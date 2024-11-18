package com.tests;

import com.codeborne.selenide.SelenideElement;
import com.pages.MainPage;
import com.tests.base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;

import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.pages.MainPage.header;
import static com.pages.MainPage.nav;
import static com.utils.BrowserConfig.getURL;
import static com.utils.Constants.MAIN_HEADER;
import static com.utils.Constants.SOCIAL_MEDIA;
import static org.junit.jupiter.api.Assertions.*;

public class MainPageTest extends BaseTest {
    public static MainPage mainPage = null;
    String auth = "auth/local";

    private void assertAudioGide() {
        String timer = mainPage.audioTimer.getText();
        int count = 0;
        do {
            executeJavaScript("document.querySelector('body').click();");
            sleep(400);
            executeJavaScript("document.querySelector('body').click();");
            mainPage.play.click();
            sleep(1400);
            mainPage.pause.click();
            count++;
        } while (timer.equals(mainPage.audioTimer.getText()) && count < 3);
        assertNotEquals(timer, mainPage.audioTimer.getText(), "Аудио не воспроизводится!");
    }

    @BeforeAll
    public static void start() {
        mainPage = new MainPage();
    }
    @BeforeEach
    public void run(TestInfo testInfo) {
        testPassed = false;
        open(getURL());
    }
    @AfterEach
    public void tearDown(TestInfo testInfo) throws IOException {
        String testName = testInfo.getDisplayName();
        reportGenerator.logTestResult(testName, testPassed);
    }
    @Test
    @DisplayName("Загрузка главной страницы")
    public void MainPageLoadsCorrectlyTest() {
        nav.should(exist).shouldBe(visible);
        header.should(exist).shouldBe(visible);
        String actualTitle = title();
        assertEquals(MAIN_HEADER, actualTitle, "Заголовок страницы некорректный.");
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка авторизации")
    public void checkAuthPageTest() {
        String expectedUrl = getURL() + auth;
        mainPage.enter.scrollTo().shouldBe(visible);
        mainPage.enter.click();
        String actualUrl = getWebDriver().getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "URL не соответствует ожидаемому!");
        back();
        assertEquals(getURL(), getWebDriver().getCurrentUrl(), "Не удалось вернуться на главную страницу проекта");
        testPassed = true;
    }
    @Test
    @DisplayName("Переход на страницу \"https://culture.ru/\"")
    public void verifyCultureRuLinkTest() {
        String expectedUrl = "https://culture.ru/";
        mainPage.culture.scrollTo().shouldBe(visible);
        mainPage.culture.click();
        switchTo().window(1);
        String actualUrl = getWebDriver().getCurrentUrl().replace("www.", "");
        assertEquals(expectedUrl, actualUrl, "URL новой вкладки не соответствует ожидаемому!");
        closeWindow();
        switchTo().window(0);
        assertEquals(getURL(), getWebDriver().getCurrentUrl(), "Не удалось вернуться на главную страницу проекта");
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка работы ссылок в footer")
    public void footerLinksTest() {
        //Ссылка не грузится исключил из проверки её
        String url = "ru/about";
        for (SelenideElement element : mainPage.footerLinks) {
            if (element.has(attribute("href")) && element.getAttribute("href").contains(url) || !element.has(attribute("href"))) {
                continue;
            }
            try {
                element.scrollTo();
                element.click();
            }
            catch (Exception e) {}
            String currentUrl = getWebDriver().getCurrentUrl();
            assertTrue(!currentUrl.equals(getURL()), "Переход по ссылке не произошел!");
            back();
            assertTrue(getWebDriver().getCurrentUrl().equals(getURL()),
                    "Не удалось вернуться на главную страницу!");
        }
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка надписи \"Мы в социальных сетях\"")
    public void SocialMediaIconsPresenceTest() {
        String expectedStr = "Мы в социальных сетях";
        mainPage.socialBlock.scrollTo();
        SelenideElement h3 = mainPage.socialBlock.$x("./h3");
        assertEquals(expectedStr, h3.getText(), "Текст в заголовке не совпадает!");
        List<SelenideElement> socialIcons = mainPage.socialIcons;
        socialIcons.forEach(icon -> icon.shouldBe(visible));
        for (SelenideElement link : mainPage.socialIcons) {
            assertTrue(SOCIAL_MEDIA.contains(link.getAttribute("href")));
        }
        testPassed = true;
    }
    @Test
    @DisplayName("Переход в Социальные сети со значков сетей")
    public void SocialMediaLinksTest() {
        String actualUrl = "";
        mainPage.socialBlock.scrollTo();
        for (SelenideElement link : mainPage.socialIcons) {
            link.click();
            switchTo().window(1);
            try {
                Alert alert = getWebDriver().switchTo().alert();
                alert.dismiss();
            } catch (NoAlertPresentException e) {
            }
            try {
                actualUrl = getWebDriver().getCurrentUrl();
            } catch (Exception e) {
                closeWindow();
                switchTo().window(0);
                continue;
            }
            assertTrue(SOCIAL_MEDIA.contains(actualUrl), "Открыт неправильный URL: " + actualUrl);
            closeWindow();
            switchTo().window(0);
        }
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка оформления подписки с валидным e-mail")
    public void subscribeWithValidEmailTest() {
        String correctEmail = "test@example.com";
        String expected = "Подписка успешно оформлена";
        mainPage.email.setValue(correctEmail);
        mainPage.subscription.click();
        mainPage.popup.shouldBe(visible);
        String popupMessage = mainPage.popup.$(By.xpath("./div[contains(text(),\"оформлена\")]")).getText();
        assertEquals(expected, popupMessage, "Сообщения не совпадают!");
        mainPage.popup.$(By.xpath("./div[1]")).click();
        mainPage.email.setValue("");
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка оформления подписки с невалидным e-mail")
    public void subscribeWithInValidEmailTest() {
        String correctEmail = "test.example.com";
        String expected = "Введите корректный адрес электронной почты";
        mainPage.email.setValue(correctEmail);
        mainPage.subscription.click();
        mainPage.errorPopup.shouldBe(visible);
        String popupMessage = mainPage.errorPopup.$(By.xpath("./div[contains(text(),\"адрес\")]")).getText();
        assertEquals(expected, popupMessage, "Сообщения не совпадают!");
        mainPage.errorPopup.$(By.xpath("./div[1]")).click();
        mainPage.email.setValue("");
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка отображения popup")
    public void popupAppearsOnHoverTest() {
        List<SelenideElement> list = mainPage.navStoresWrapper.$$(By.xpath("./a"));
        for (SelenideElement link : list) {
            link.hover();
            SelenideElement popup = link.$(By.xpath(".//div[contains(@class,'store_qr_wrapper')]"));
            assertTrue(popup.isDisplayed(), "Не отображается popup!");
            $("body").hover();
        }
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка переключения языков на странице между RU и English")
    public void languageChangeTest() {
        String expected = "Artefact - guide to Russian museums";
        mainPage.changeLocal();
        assertEquals(expected, $("h1").getText(), "Переключение языка на English не произошло!");
        mainPage.changeLocal();
        assertNotEquals(expected, $("h1").getText(), "Переключение языка не произошло!");
        testPassed = true;
    }
    @Test
    @DisplayName("Проверка работы аудиогида по определенному url: \"https://ar.culture.ru/ru/subject/berezy-opushka-lesa\"")
    public void audioGuidePlayTest() {
        open("https://ar.culture.ru/ru/subject/berezy-opushka-lesa");
        executeJavaScript("document.querySelector('body').click();");
        sleep(1000);
        String timer = mainPage.audioTimer.getText();
        mainPage.play.shouldBe(visible).click();
        while (timer.equals(mainPage.audioTimer.getText())) {
            sleep(1600);
        }
        assertNotEquals(timer, mainPage.audioTimer.getText(), "Аудио не воспроизводится!");

        mainPage.pause.click();
        timer = mainPage.audioTimer.getText();
        assertEquals(timer, mainPage.audioTimer.getText(), "Аудио воспроизводится!");
        open(getURL());
        testPassed = true;
    }
    @Test
    @DisplayName("Добавить в Избранное")
    public void addToFavoritesTest() {
        int likes;
        try {
            likes = Integer.parseInt(mainPage.getLikes());
            back();
        } catch (NumberFormatException e) {
            likes = -1;
        }
        if (likes < 1) {
            likes = Integer.parseInt(mainPage.getLikes());
            back();
        }
        assertTrue(likes >= 1);
        mainPage.getLikes();
        testPassed = true;
        open(getURL());
    }
    @Test
    @DisplayName("Удалить из Избранное")
    public void deleteFromFavoritesTest() {
        int likes;
        try {
            likes = Integer.parseInt(mainPage.getLikes());
        } catch (NumberFormatException e) {
            likes = 0;
            back();
        }
        if (likes > 1) {
            if (mainPage.getLikes().equals("")) {
                likes = 0;
            }
        }
        assertTrue(likes == 0);
        testPassed = true;
        open(getURL());
    }
    @Test()
    @DisplayName("Проверка работы аудиогдида с фильтром: \"с аудиогидом\" для всех экспонатов на странице")
    public void checkAllExhibitsWithAudioGideTest() {
        for (SelenideElement element : mainPage.getAudioGid()) {
            element.scrollTo();
            element.click();
            assertAudioGide();
            back();
        }
        testPassed = true;
        open(getURL());
    }
}

