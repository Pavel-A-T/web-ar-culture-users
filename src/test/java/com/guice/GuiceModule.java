package com.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.pages.MainPage;
import com.pages.SearchPage;


public class GuiceModule extends AbstractModule {
  @Singleton
  @Provides
  public MainPage getMainPage() {
    return new MainPage();
  }

  @Singleton
  @Provides
  public SearchPage getSearchPage() {
    return new SearchPage();
  }
}
