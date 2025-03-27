package com.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

@Getter
@Setter
public class SearchPage extends AbsBasePage<SearchPage> {
  private SelenideElement search = $x("//div[@id=\"search_switcher\"]");
  private SelenideElement searchInput = $x("//input[contains(@placeholder, \"Поиск по музеям\")]");
  private SelenideElement findElement = $x("//div[contains(@class, \"inner_content\")]/div[contains(text(),\"Найти\")]");
  private SelenideElement result = $x("//h1[contains(text(),\"Результаты\")]");
  private SelenideElement title = $x("//div[contains(@class,\"listing_block_title\")]");
  ElementsCollection listObjects  = $$x("//div[contains(@class,\"blocks_list_wrapper\")]/a");

    public SelenideElement getElement(String text) {
        return $x("//h1[contains(text(),\"" + text +  "\")]");
    }
}
