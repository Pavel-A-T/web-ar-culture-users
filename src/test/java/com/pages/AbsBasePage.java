package com.pages;

import com.codeborne.selenide.Selenide;

public abstract class AbsBasePage<T> {
  public T open(String path) {
    Selenide.open(path);
    return (T) this;
  }
  public T open() {
    Selenide.open("");
    return (T) this;
  }
}
