package com.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;

import static com.codeborne.selenide.Selenide.*;

@Getter
@Setter
public class MainPage extends AbsBasePage<MainPage> {
  public SelenideElement cookieAcceptButton = $(".cookies_accept");
  private SelenideElement nav = $x("//nav");
  private SelenideElement header = $x("//h1");
  private SelenideElement enter = $x("//a[@href=\"/auth/local\" and contains(text(), \"Войти \")]");
  private SelenideElement culture = $x("//a[@href=\"http://culture.ru\"]");
  private ElementsCollection footerLinks = $$x("//div[contains(@class,\"footer_columns\")]/div/a");
  private ElementsCollection socialIcons = $$x("//div[contains(@class,\"socials_block\")]/div/a");
  private SelenideElement socialBlock = $x("//div[contains(@class,\"socials_block\")]");
  private SelenideElement subscription = $x("//div[contains(@class,\"send_subscription_email\")]");
  private SelenideElement email = $x("//input[contains(@type,\"email\")]");
  private SelenideElement popup = $x("//div[@id=\"subscription_info_popup\"]");
  private SelenideElement errorPopup = $x("//div[@id=\"subscription_error_popup\"]");
  private SelenideElement navStoresWrapper = $x("//div[contains(@class, \"nav_stores_wrapper\")]");
  private SelenideElement local = $x("//div[contains(@class,\"local\")]");
  private SelenideElement project = $x("//li[contains(@class,\"about_project_menu\")]");
  private SelenideElement author = $x("//a[contains(text(),\"Авторы\")]");
  private ElementsCollection listingBlock = $$x("//a[contains(@class,\"listing_block\")]");
  private SelenideElement play = $x("//div[contains(@id,\"audio_play\")]");
  private SelenideElement pause = $x("//div[contains(@id,\"audio_pause\")]");
  private SelenideElement audioTimer = $x("//div[contains(@id,\"audio_time\")]");
  private SelenideElement like = $x("//div[contains(@class,\"like_wrapper\")]");
  private SelenideElement likeBtn = $x("//div[contains(@class,\"add_like_btn\") and contains(@class,\"active\"]");
  private SelenideElement countLikes = $x("//div[contains(@class,\"add_like\")]/span");
  private SelenideElement museum = $x("//ul[contains(@class,\"menu\")]/li/a[contains(@href,\"/ru/museums\") and text()=\"Музеи\"]");
  private ElementsCollection museums = $$x("//a[contains(@class,\"listing_block\")]");
  private SelenideElement exhibits = $x("//a[contains(text(),\"Экспонаты\")]");
  private SelenideElement audioGide = $x("//a[contains(text(), \"С аудиогидом\")]");
  private SelenideElement all = $x("//div/a[contains(text(),\"Посмотреть все\")]");
  private SelenideElement blockSort = $x("//div[contains(@id,\"blocks_sort\")]");
  private SelenideElement sortAsc = $x("//div[contains(@id,\"blocks_sort_list\")]/div[contains(text(),\"по названию (А-Я)\")]");
  private SelenideElement sortDesc = $x("//div[contains(@id,\"blocks_sort_list\")]/div[contains(text(),\"по названию (Я-А)\")]");
  private SelenideElement readButton = $x("//div[contains(@id,\"show_full_article_button\")]");
  private SelenideElement moreMaterials = $x("//div[contains(@class,\"subjects_links_right\")]/a[contains(text(),\"С доп\")]");

  private boolean checkButton(int exhibit) {
    exhibits.click();
    moreMaterials.click();
    ElementsCollection list = $$x("//div[contains(@class,\"blocks_list_wrapper\")]/a[contains(@class, \"listing_block\")]");
    ((ElementsCollection) list).shouldBe(CollectionCondition.sizeGreaterThan(1));
    if (exhibit < list.size() && exhibit >= 0) {
      SelenideElement element = list.get(exhibit);
      element.hover();
      element.click();
    } else {
      return false;
    }
    readButton.scrollTo();
    readButton.click();
    return true;
  }

  public void changeLocal() {
    local.click();
  }

  public String getLikes() {
    museum.click();
    ElementsCollection blocks = $$x("//div[contains(@class,\"blocks_list_wrapper\")]");
    SelenideElement mus = blocks.get(0).$x("./a");
    mus.click();
    like.click();
    return countLikes.getText().trim();
  }

  public boolean disLike() {
    museum.click();
    ElementsCollection blocks = $$x("//div[contains(@class,\"blocks_list_wrapper\")]");
    SelenideElement mus = blocks.get(0).$x("./a");
    mus.click();
    ElementsCollection list = $$x("//div[contains(@class,\"add_like_btn\") and contains(@class,\"active\")]");
    if (list.size() > 0) {
      like.click();
    }
    return $$x("//div[contains(@class,\"add_like_btn\") and contains(@class,\"active\")]").size() == 0;
  }

  public ElementsCollection getAudioGid() {
    exhibits.click();
    audioGide.click();
    ElementsCollection list = $$x("//div[contains(@class,\"blocks_list_wrapper\")]/a[contains(@class, \"listing_block\")]");
    ((ElementsCollection) list).shouldBe(CollectionCondition.sizeGreaterThan(1));
    return list;
  }

  public void getExhibits() {
    exhibits.click();
    all.scrollTo();
    all.click();
  }

  public void getPagination(String page) {
    SelenideElement element = $x("//div/a[contains(@href,\"" + page + "\")]");
    element.click();
  }

  public ElementsCollection getSortAscExhibits(String sort) {
    getExhibits();
    blockSort.scrollTo();
    blockSort.click();
    if (sort.equalsIgnoreCase("asc")) {
      sortAsc.click();
    } else {
      sortDesc.click();
    }
    ElementsCollection list = $$x("//div[contains(@class,\"blocks_list_wrapper\")]/a[contains(@class, \"listing_block\")]/div/div/div[contains(@class, \"listing_block_title\")]");
    list.shouldBe(CollectionCondition.sizeGreaterThan(1));
    return list;
  }

  public boolean checkButtonRead(int exhibit) {
    if (checkButton(exhibit)) {
      readButton.scrollTo();
      if (readButton.getText().equalsIgnoreCase("скрыть")) {
        return true;
      }
    }
    return false;
  }

  public boolean checkHideButton(int exhibit) {
    if (checkButton(exhibit)) {
      readButton.scrollTo();
      readButton.click();
      readButton.scrollTo();
      if (readButton.getText().equalsIgnoreCase("читать дальше")) {
        return true;
      }
    }
    return false;
  }
}
