package com.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.pages.SearchPage;
import com.tests.base.BaseTest;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;
import static com.utils.BrowserConfig.getURL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SearchTest extends BaseTest {
    public static SearchPage searchPage = null;

    @BeforeAll
    public static void start() {
        searchPage = new SearchPage();
    }
    @BeforeEach
    public void run(TestInfo testInfo) {
        open(getURL());
        testPassed = false;
    }
    @AfterEach
    public void tearDown(TestInfo testInfo) throws IOException {
        String testName = testInfo.getDisplayName();
        reportGenerator.logTestResult(testName, testPassed);
    }
    @Test
    @DisplayName("Проверка работы поиска на примере поиска: \"Тихвинский музей\"")
    public void searchTest() {
        String searchString = "Тихвинский музей";
        String result = "Результаты поиска";
        searchPage.search.click();
        searchPage.searchInput.setValue(searchString);
        searchPage.findElement.click();
        assertEquals(searchString, searchPage.title.getText(), "Названия не совпадают!");
        assertEquals(result, searchPage.result.getText(), "Названия не совпадают!");
        testPassed = true;
        open(getURL());
    }

    @Test
    @DisplayName("Возможность просмотра различного контента на странице")
    public void cultureObjectPageLoadTest() {
        for (SelenideElement element : searchPage.listObjects) {
            if (!element.is(Condition.visible)) {
                continue;
            }
            String title = Selenide.title();
            String expected = element.getText();
            element.click();
            String actualValue = searchPage.getElement(expected).getText();
            assertEquals(expected, actualValue, "Названия не совпадают!");
            assertNotEquals(title, Selenide.title(), "Названия вкладок web страницы не должны совпадать!");
            testPassed = true;
            back();
        }
    }
}

