package com.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MainPage {
    @FindBy(css = ".cookies_accept")
    public static SelenideElement cookieAcceptButton;
    @FindBy(xpath = "//nav")
    public static SelenideElement nav;
    @FindBy(xpath = "//h1")
    public static SelenideElement header;
    @FindBy(xpath = "//a[@href=\"/auth/local\" and contains(text(), \"Войти \")]")
    public SelenideElement enter;
    @FindBy(xpath = "//a[@href=\"http://culture.ru\"]")
    public SelenideElement culture;
    @FindBy(xpath = "//div[contains(@class,\"footer_columns\")]/div/a")
    public List<SelenideElement> footerLinks;
    @FindBy(xpath = "//div[contains(@class,\"socials_block\")]/div/a")
    public List<SelenideElement> socialIcons;
    @FindBy(xpath = "//div[contains(@class,\"socials_block\")]")
    public SelenideElement socialBlock;
    @FindBy(xpath = "//div[contains(@class,\"send_subscription_email\")]")
    public SelenideElement subscription;
    @FindBy(xpath = "//input[contains(@type,\"email\")]")
    public SelenideElement email;
    @FindBy(xpath = "//div[@id=\"subscription_info_popup\"]")
    public SelenideElement popup;
    @FindBy(xpath = "//div[@id=\"subscription_error_popup\"]")
    public SelenideElement errorPopup;
    @FindBy(xpath = "//div[contains(@class, \"nav_stores_wrapper\")]")
    public SelenideElement navStoresWrapper;
    @FindBy(xpath = "//div[contains(@class,\"local\")]")
    SelenideElement local;
    @FindBy(xpath = "//li[contains(@class,\"about_project_menu\")]")
    SelenideElement project;
    @FindBy(xpath = "//a[contains(text(),\"Авторы\")]")
    SelenideElement author;
    @FindBy(xpath = "//a[contains(@class,\"listing_block\")]")
    List<SelenideElement> listingBlock;
    @FindBy(xpath = "//div[contains(@id,\"audio_play\")]")
    public SelenideElement play;
    @FindBy(xpath = "//div[contains(@id,\"audio_pause\")]")
    public SelenideElement pause;
    @FindBy(xpath = "//div[contains(@id,\"audio_time\")]")
    public SelenideElement audioTimer;
    @FindBy(xpath = "//div[contains(@class,\"like_wrapper\")]")
    public SelenideElement like;
    @FindBy(xpath = "//div[contains(@class,\"add_like\")]/span")
    public SelenideElement countLikes;
    @FindBy(xpath = "//ul[contains(@class,\"menu\")]/li/a[contains(@href,\"/ru/museums\") and text()=\"Музеи\"]")
    SelenideElement museum;
    @FindBy(xpath = "//a[contains(@class,\"listing_block\")]")
    List<SelenideElement> museums;
    @FindBy(xpath = "//a[contains(text(),\"Экспонаты\")]")
    SelenideElement exhibits;
    @FindBy(xpath = "//a[contains(text(), \"С аудиогидом\")]")
    SelenideElement audioGide;


    public void changeLocal() {
        local.click();
    }

    public String getLikes() {
        museum.click();
        List<SelenideElement> blocks = $$(By.xpath("//div[contains(@class,\"blocks_list_wrapper\")]"));
        SelenideElement mus = blocks.get(0).$(By.xpath("./a"));
        mus.click();
        like.click();
        return countLikes.getText().trim();
    }

    public List<SelenideElement> getAudioGid() {
        exhibits.click();
        audioGide.click();
        List<SelenideElement> list = $$(By.xpath("//div[contains(@class,\"blocks_list_wrapper\")]/a[contains(@class, \"listing_block\")]"));
        ((ElementsCollection) list).shouldBe(CollectionCondition.sizeGreaterThan(1));
        return list;
    }


    public MainPage() {
        page(this);
    }
}
