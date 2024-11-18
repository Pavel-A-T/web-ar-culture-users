package com.tests.base;

import com.pages.MainPage;
import com.utils.BrowserConfig;
import com.utils.Constants;
import com.utils.ReportGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.pages.MainPage.cookieAcceptButton;
import static com.utils.BrowserConfig.getURL;

public class BaseTest {
    private static int count = 0;
    public static boolean testPassed;
    public static ReportGenerator reportGenerator;
    private static long startTime = System.currentTimeMillis();

    static {
        try {
            reportGenerator = new ReportGenerator();
        } catch (Exception e) {
        }
    }
    @BeforeAll
    public static void setUp() {
        BrowserConfig.setup(Constants.CHROME);
        open(getURL());
        new MainPage();
        if (cookieAcceptButton.isDisplayed()) {
            cookieAcceptButton.click();
        }
    }
    @AfterAll
    public static void tearDown() {
        //Количество классов с тестами, чтобы создавать только Один *.log отчет по всем классам.
        count++;
        if (count == 3) {
            reportGenerator.closeLog(System.currentTimeMillis() - startTime);
        }
        closeWebDriver();
    }
}
