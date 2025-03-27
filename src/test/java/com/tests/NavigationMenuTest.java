package com.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.extentions.UIExtension;
import com.google.inject.Inject;
import com.pages.MainPage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.title;
import static com.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(UIExtension.class)
public class NavigationMenuTest {
  @Inject
  private MainPage mainPage;

  @Test
  @Step("Проверка работы навигационного меню")
  @DisplayName("Проверка работы навигационного меню")
  public void navMenuTest() {
    mainPage.open();
    int menuSize = $$("ul.menu li a").size();
    List<String> requiredTitles = Arrays.asList(MUSEUM, EXHIBIT, EXHIBITION, QUIZ);
    for (int i = 0; i < menuSize; i++) {
      String menuText = $$("ul.menu li a").get(i).text();
      SelenideElement nav = $$("ul.menu li a").get(i);
      if (nav.is(Condition.visible)) {
        nav.click();
      }
      if (!requiredTitles.contains(menuText)) {
        continue;
      }
      switch (menuText) {
        case MUSEUM:
          assertEquals(MUSEUM_HEADER, title(), "Заголовок некорректный.");
          mainPage.open();
          break;
        case EXHIBITION:
          assertEquals(EXHIBITION_HEADER, title(), "Заголовок некорректный.");
          mainPage.open();
          break;
        case EXHIBIT:
          assertEquals(EXHIBIT_HEADER, title(), "Заголовок некорректный.");
          mainPage.open();
          break;
        case QUIZ:
          assertEquals(QUIZ_HEADER, title(), "Заголовок некорректный.");
          mainPage.open();
          break;
      }
    }
  }
}
