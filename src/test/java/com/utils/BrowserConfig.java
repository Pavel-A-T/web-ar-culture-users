package com.utils;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserConfig {
    public static void setup(String browser) {
        if (browser.equals(Constants.CHROME)) {
            WebDriverManager.chromedriver().setup();
            Configuration.browser = "chrome";
        } else if (browser.equals(Constants.FIREFOX)) {
            WebDriverManager.firefoxdriver().setup();
            Configuration.browser = "firefox";
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        Configuration.browserSize = "1980x1080";
        Configuration.browserVersion = "";
        Configuration.holdBrowserOpen = true;
        String baseUrl = getURL();
        if (baseUrl == null) {
            throw new IllegalStateException("Base URL is not defined!");
        }
        Configuration.baseUrl = baseUrl;
    }
    public static String getURL() {
        return ConfigReader.get("url");
    }
}
