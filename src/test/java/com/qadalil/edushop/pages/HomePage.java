package com.qadalil.edushop.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public final class HomePage extends BasePage {
  private final By cards = By.cssSelector(".product-card");

  public HomePage(WebDriver driver) { super(driver); }

  public HomePage open() {
    driver.get(System.getProperty("baseUrl", "http://localhost:4200"));
    visible(By.cssSelector(".hero-title"));
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(cards, 0));
    return this;
  }

  public HomePage category(String name) {
    new Select(visible(By.cssSelector(".filter-bar select.form-select:first-of-type"))).selectByValue(name);
    wait.until(ExpectedConditions.textToBePresentInElementLocated(
        By.cssSelector(".product-card .category-badge"), name));
    return this;
  }

  public HomePage search(String term) {
    type(By.cssSelector(".hero-search input"), term);
    click(By.cssSelector(".hero-search button"));
    wait.until(ExpectedConditions.textToBePresentInElementLocated(
        By.cssSelector(".product-card .card-title"), term));
    return this;
  }

  public int cardCount() { return driver.findElements(cards).size(); }

  public String firstProductName() {
    return visible(By.cssSelector(".product-card .card-title")).getText();
  }

  public ProductPage openFirstProduct() {
    click(By.cssSelector(".product-card a[href^='/products/']"));
    return new ProductPage(driver);
  }

  public HomePage addFirstAvailableToCart() {
    List<WebElement> products = driver.findElements(cards);
    for (WebElement product : products) {
      WebElement add = product.findElement(By.cssSelector("button.btn-primary"));
      if (add.isEnabled()) {
        add.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        return this;
      }
    }
    throw new AssertionError("No product with available stock on the current page");
  }
}
