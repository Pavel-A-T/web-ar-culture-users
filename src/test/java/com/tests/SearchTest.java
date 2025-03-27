package com.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.extentions.UIExtension;
import com.google.inject.Inject;
import com.pages.SearchPage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(UIExtension.class)
public class SearchTest {
    @Inject
    public SearchPage searchPage;

    @Test
    @Step("Проверка работы поиска на примере поиска: \"Тихвинский музей\"")
    @DisplayName("Проверка работы поиска на примере поиска: \"Тихвинский музей\"")
    public void searchTest() {
        String searchString = "Тихвинский музей";
        String result = "Результаты поиска";
        searchPage.open("");
        searchPage.getSearch().click();
        searchPage.getSearchInput().setValue(searchString);
        searchPage.getFindElement().click();
        assertEquals(searchString, searchPage.getTitle().getText(), "Названия не совпадают!");
        assertEquals(result, searchPage.getResult().getText(), "Названия не совпадают!");
    }
    @Test
    @Step("Возможность просмотра различного контента на странице")
    @DisplayName("Возможность просмотра различного контента на странице")
    public void cultureObjectPageLoadTest() {
      searchPage.open("");
      for (SelenideElement element : searchPage.getListObjects()) {
            if (!element.is(Condition.visible)) {
                continue;
            }
            String title = Selenide.title();
            String expected = element.getText();
            element.click();
            String actualValue = searchPage.getElement(expected).getText();
            assertEquals(expected, actualValue, "Названия не совпадают!");
            assertNotEquals(title, Selenide.title(), "Названия вкладок web страницы не должны совпадать!");
        }
    }
}

