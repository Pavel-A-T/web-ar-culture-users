package com.exceptions;

public class BrowserNotSupportedException extends RuntimeException {
  public BrowserNotSupportedException(String browserName) {
    super(browserName + " - Неподдерживаемый браузер!");
  }
}