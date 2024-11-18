package com.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.tests.base.BaseTest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.utils.BrowserConfig.getURL;
import static com.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NavigationMenuTest extends BaseTest {
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
    @DisplayName("Проверка работы навигационного меню")
    public void navMenuTest() {
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
                    open(getURL());
                    break;
                case EXHIBITION:
                    assertEquals(EXHIBITION_HEADER, title(), "Заголовок некорректный.");
                    open(getURL());
                    break;
                case EXHIBIT:
                    assertEquals(EXHIBIT_HEADER, title(), "Заголовок некорректный.");
                    open(getURL());
                    break;
                case QUIZ:
                    assertEquals(QUIZ_HEADER, title(), "Заголовок некорректный.");
                    open(getURL());
                    break;
            }
        }
        testPassed = true;
        open(getURL());
    }
}
