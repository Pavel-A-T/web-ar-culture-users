package com.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SearchPage {
    @FindBy(xpath = "//div[@id=\"search_switcher\"]")
    public SelenideElement search;
    @FindBy(xpath = "//input[contains(@placeholder, \"Поиск по музеям\")]")
    public SelenideElement searchInput;
    @FindBy(xpath = "//div[contains(@class, \"inner_content\")]/div[contains(text(),\"Найти\")]")
    public SelenideElement findElement;
    @FindBy(xpath = "//h1[contains(text(),\"Результаты\")]")
    public SelenideElement result;
    @FindBy(xpath = "//div[contains(@class,\"listing_block_title\")]")
    public SelenideElement title;
    @FindBy(xpath = "//div[contains(@class,\"blocks_list_wrapper\")]/a")
    public List<SelenideElement> listObjects;

    public SelenideElement getElement(String text) {
        return $(By.xpath("//h1[contains(text(),\"" + text +  "\")]"));
    }

    public SearchPage() {
        page(this);
    }
}
