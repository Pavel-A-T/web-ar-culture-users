package com.extentions;

import com.codeborne.selenide.Configuration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.guice.GuiceModule;
import com.utils.ConfigReader;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;

public class UIExtension implements BeforeAllCallback, BeforeEachCallback {
  private Injector injector;

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    Configuration.baseUrl = System.getProperty("base.url", ConfigReader.getProperty("base.url"));
    Configuration.browser = System.getProperty("browser.name", ConfigReader.getProperty("browser.name"));
    Dimension screenSize = getScreenSize();
    Configuration.browserSize = screenSize.width + "x" + screenSize.height;
    if ("chrome".equalsIgnoreCase(Configuration.browser)) {
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--disable-extensions");
      Configuration.browserCapabilities = options;
    }
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    injector = Guice.createInjector(new GuiceModule());
    injector.injectMembers(extensionContext.getTestInstance().get());
  }

  private Dimension getScreenSize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
  }
}
