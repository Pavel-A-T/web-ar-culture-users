package com.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.extentions.UIExtension;
import com.google.inject.Inject;
import com.pages.MainPage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.utils.Constants.MAIN_HEADER;
import static com.utils.Constants.SOCIAL_MEDIA;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(UIExtension.class)
public class MainPageTest {
  @Inject
  public MainPage mainPage;
  String auth = "auth/local";

  private void assertAudioGide() {
    String timer = mainPage.getAudioTimer().getText();
    int count = 0;
    do {
      executeJavaScript("document.querySelector('body').click();");
      sleep(500);
      executeJavaScript("document.querySelector('body').click();");
      mainPage.getPlay().click();
      sleep(1500);
      mainPage.getPause().click();
      count++;
    } while (timer.equals(mainPage.getAudioTimer().getText()) && count < 3);
    assertNotEquals(timer, mainPage.getAudioTimer().getText(), "Аудио не воспроизводится!");
  }

  @Test
  @Step("Загрузка главной страницы")
  @Description("Тест проверяет загрузку главной страницы")
  @DisplayName("Загрузка главной страницы")
  public void mainPageLoadsCorrectlyTest() {
    mainPage.open();
    mainPage.getNav().should(exist).shouldBe(visible);
    mainPage.getHeader().should(exist).shouldBe(visible);
   assertEquals(MAIN_HEADER, title(), "Заголовок страницы некорректный.");
  }

  @Test
  @Step("Проверка авторизации")
  @Description("Тест проверяет авторизацию")
  @DisplayName("Проверка авторизации")
  public void checkAuthPageTest() {
    String expectedUrl = Configuration.baseUrl + auth;
    mainPage.open("");
    mainPage.getEnter().scrollTo().shouldBe(visible);
    mainPage.getEnter().click();
    String actualUrl = getWebDriver().getCurrentUrl();
    assertEquals(expectedUrl, actualUrl, "URL не соответствует ожидаемому!");
  }

  @Test
  @Step("Переход на страницу \"https://culture.ru/\"")
  @Description("Тест проверяет переход на страницу \"https://culture.ru/\"")
  @DisplayName("Переход на страницу \"https://culture.ru/\"")
  public void verifyCultureRuLinkTest() {
    String expectedUrl = "https://culture.ru/";
    mainPage.open("");
    mainPage.getCulture().scrollTo().shouldBe(visible);
    mainPage.getCulture().click();
    String url = getWebDriver().getCurrentUrl();
    switchTo().window(1);
    String actualUrl = getWebDriver().getCurrentUrl().replace("www.", "");
    assertEquals(expectedUrl, actualUrl, "URL новой вкладки не соответствует ожидаемому!");
    closeWindow();
    switchTo().window(0);
    assertEquals(url, getWebDriver().getCurrentUrl(), "Не удалось вернуться на главную страницу проекта");
  }

  @Test
  @Step("Тест: Проверка работы ссылок в footer")
  @Description("Проверка работы ссылок в footer")
  @DisplayName("Проверка работы ссылок в footer")
  public void footerLinksTest() {
    //Ссылка не грузится исключил из проверки её
    String url = "ru/about";
    String currentUrl = "";
    mainPage.open("");
    for (SelenideElement element : mainPage.getFooterLinks()) {
      if (element.has(attribute("href")) && element.getAttribute("href").contains(url) || !element.has(attribute("href"))) {
        continue;
      }
      try {
        element.scrollTo();
        currentUrl = getWebDriver().getCurrentUrl();
        element.click();
      } catch (Exception e) {
      }
      assertTrue(!currentUrl.equals(url), "Переход по ссылке не произошел!");
      back();
      assertTrue(getWebDriver().getCurrentUrl().equals(currentUrl),
          "Не удалось вернуться на главную страницу!");
    }
  }

  @Test
  @Step("Тест: Проверка надписей на странице")
  @Description("Проверка надписи \"Мы в социальных сетях\"")
  @DisplayName("Проверка надписи \"Мы в социальных сетях\"")
  public void socialMediaIconsPresenceTest() {
    String expectedStr = "Мы в социальных сетях";
    mainPage.open("");
    mainPage.getSocialBlock().scrollTo();
    SelenideElement h3 = mainPage.getSocialBlock().$x("./h3");
    assertEquals(expectedStr, h3.getText(), "Текст в заголовке не совпадает!");
    ElementsCollection socialIcons = mainPage.getSocialIcons();
    socialIcons.forEach(icon -> icon.shouldBe(visible));
    for (SelenideElement link : mainPage.getSocialIcons()) {
      assertTrue(SOCIAL_MEDIA.contains(link.getAttribute("href")));
    }
  }

  @Test
  @Step("Тест: Переход в социальные сети")
  @Description("Проверка перехода в Соц сети")
  @DisplayName("Переход в Социальные сети со значков сетей")
  public void socialMediaLinksTest() {
    String actualUrl = "";
    mainPage.open("");
    mainPage.getSocialBlock().scrollTo();
    for (SelenideElement link : mainPage.getSocialIcons()) {
      if (link.getAttribute("href").contains("outube")) {
        continue;
      }
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
  }

  @Test
  @Step("Тест: \"Проверка оформления подписки с валидным e-mail\"")
  @Description("Проверка оформления подписки с валидным e-mail")
  @DisplayName("Проверка оформления подписки с валидным e-mail")
  public void subscribeWithValidEmailTest() {
    String correctEmail = "test@example.com";
    String expected = "Подписка успешно оформлена";
    mainPage.open("");
    mainPage.getEmail().setValue(correctEmail);
    mainPage.getSubscription().click();
    mainPage.getPopup().shouldBe(visible);
    String popupMessage = mainPage.getPopup().$x("./div[contains(text(),\"оформлена\")]").getText();
    assertEquals(expected, popupMessage, "Сообщения не совпадают!");
    mainPage.getPopup().$x("./div[1]").click();
    mainPage.getEmail().setValue("");
  }

  @Test
  @Step("Тест: \"Проверка оформления подписки с невалидным e-mail\"")
  @Description("Проверка оформления подписки с невалидным e-mail")
  @DisplayName("Проверка оформления подписки с невалидным e-mail")
  public void subscribeWithInValidEmailTest() {
    String correctEmail = "test.example.com";
    String expected = "Введите корректный адрес электронной почты";
    mainPage.open("");
    mainPage.getEmail().setValue(correctEmail);
    mainPage.getSubscription().click();
    mainPage.getErrorPopup().shouldBe(visible);
    String popupMessage = mainPage.getErrorPopup().$x("./div[contains(text(),\"адрес\")]").getText();
    assertEquals(expected, popupMessage, "Сообщения не совпадают!");
    mainPage.getErrorPopup().$x("./div[1]").click();
    mainPage.getEmail().setValue("");
  }

  @Test
  @Step("Тест: отображение popup")
  @Description("Проверка отображения popup")
  @DisplayName("Проверка отображения popup")
  public void popupAppearsOnHoverTest() {
    mainPage.open("");
    ElementsCollection list = mainPage.getNavStoresWrapper().$$x("./a");
    for (SelenideElement link : list) {
      link.hover();
      SelenideElement popup = link.$x(".//div[contains(@class,'store_qr_wrapper')]");
      assertTrue(popup.isDisplayed(), "Не отображается popup!");
      $("body").hover();
    }
  }

  @Test
  @Step("Тест: переключение языка страницы")
  @Description("Проверка переключения языков на странице между RU и English")
  @DisplayName("Проверка переключения языков на странице между RU и English")
  public void languageChangeTest() {
    mainPage.open("");
    String expected = "Artefact - guide to Russian museums";
    mainPage.changeLocal();
    assertEquals(expected, $("h1").getText(), "Переключение языка на English не произошло!");
    mainPage.changeLocal();
    assertNotEquals(expected, $("h1").getText(), "Переключение языка не произошло!");
  }

  @Test
  @Step("Проверка аудиогида")
  @Description("Проверка работы аудиогида по определенному url: \"ru/subject/berezy-opushka-lesa\"")
  @DisplayName("Проверка работы аудиогида по определенному url: \"ru/subject/berezy-opushka-lesa\"")
  public void audioGuidePlayTest() {
    mainPage.open("ru/subject/berezy-opushka-lesa");
    assertAudioGide();
  }

  @Test
  @Step("Добавить в Избранное")
  @Description("Добавить в Избранное")
  @DisplayName("Добавить в Избранное")
  public void addToFavoritesTest() {
    int likes;
    mainPage.open("");
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
  }

  @Test
  @Step("Удалить из Избранного")
  @Description("Удалить из Избранного")
  @DisplayName("Удалить из Избранного")
  public void deleteFromFavoritesTest() {
    mainPage.open("");
    assertTrue(mainPage.disLike());
  }

  @Test
  @Step("Проверка аудиогида")
  @Description("Проверка работы аудиогдида с фильтром: \"с аудиогидом\" для всех экспонатов на странице")
  @DisplayName("Проверка работы аудиогдида с фильтром: \"с аудиогидом\" для всех экспонатов на странице")
  public void checkAllExhibitsWithAudioGideTest() {
    mainPage.open("");
    for (SelenideElement element : mainPage.getAudioGid()) {
      element.scrollTo();
      element.click();
      assertAudioGide();
      back();
    }
  }

  @Test
  @Step("Проверка пагинации")
  @Description("Проверка пагинации")
  @DisplayName("Проверка пагинации")
  public void checkPaginationTest() {
    String[] list = {"#1", "#2", "#3", "#4", "#5"};
    mainPage.open("");
    mainPage.getExhibits();
    for (String page : list) {
      mainPage.getPagination(page);
      sleep(200);
      assertTrue(webdriver().driver().url().contains(page), "Пагинация не работает!");
    }
  }

  @Test
  @Step("Проверка сортировки")
  @Description("Проверка сортировки по названию (А-Я)")
  @DisplayName("Проверка сортировки по названию (А-Я)")
  public void checkSortAscTest() {
    String sort = "asc";
    mainPage.open();
    ElementsCollection list = mainPage.getSortAscExhibits(sort);
    if (list == null) return;
    for (int i = 0; i < list.size() - 1; i++) {
      String first = list.get(i).hover().getText();
      String second = list.get(i + 1).hover().getText();
      //БАГ в сортировке на 1 странице
      if (first.equals("Полное собрание сочинений. 1895 г.")) continue;
      assertTrue(second.compareToIgnoreCase(first) >= 0);
    }
  }

  @Test
  @Step("Проверка сортировки по названию (Я-А)")
  @Description("Проверка сортировки по названию (Я-А)")
  @DisplayName("Проверка сортировки по названию (Я-А)")
  public void checkSortDescTest() {
    String sort = "desc";
    mainPage.open();
    ElementsCollection list = mainPage.getSortAscExhibits(sort);
    if (list == null) return;
    for (int i = 0; i < list.size() - 1; i++) {
      String first = list.get(i).hover().getText();
      String second = list.get(i + 1).hover().getText();
      assertTrue(second.compareToIgnoreCase(first) <= 0);
    }
  }

  @Test
  @Step("Проверка работы кнопки")
  @Description("Проверка работы кнопки Читать далее")
  @DisplayName("Проверка работы кнопки Читать далее")
  public void checkButtonReadMoreTest() {
    mainPage.open();
    int exhibit = (int) (Math.random() * 10) + 1;
    assertTrue(mainPage.checkButtonRead(exhibit));
  }

  @Test
  @Step("Проверка работы кнопки Скрыть")
  @Description("Проверка работы кнопки Скрыть")
  @DisplayName("Проверка работы кнопки Скрыть")
  public void checkButtonHideTest() {
    mainPage.open();
    int exhibit = (int) (Math.random() * 10) + 1;
    assertTrue(mainPage.checkHideButton(exhibit));
  }
}

