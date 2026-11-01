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
    wait.until(driver -> !driver.findElements(By.cssSelector(".product-card .category-badge")).isEmpty()
        && driver.findElement(By.cssSelector(".product-card .category-badge"))
            .getText().equalsIgnoreCase(name));
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

  public HomePage searchWithoutResults(String term) {
    type(By.cssSelector(".hero-search input"), term);
    click(By.cssSelector(".hero-search button"));
    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".filter-bar"), "0 products found"));
    return this;
  }

  public HomePage sortByName(boolean descending) {
    new Select(visible(By.xpath("(//div[contains(@class,'filter-bar')]//select)[2]"))).selectByValue("name");
    WebElement toggle = visible(By.cssSelector(".filter-bar input[type='checkbox']"));
    if (toggle.isSelected() != descending) toggle.click();
    wait.until(driver -> {
      List<String> names = productNames();
      if (names.size() < 2) return false;
      List<String> sorted = names.stream().sorted(descending
          ? java.util.Comparator.reverseOrder() : java.util.Comparator.naturalOrder()).toList();
      return names.equals(sorted) && driver.findElements(By.cssSelector(".spinner-overlay")).isEmpty();
    });
    return this;
  }

  public List<String> productNames() {
    return driver.findElements(By.cssSelector(".product-card .card-title"))
        .stream().map(WebElement::getText).toList();
  }

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
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", add);
        clickable(By.cssSelector(".product-card button.btn-primary:not([disabled])")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        return this;
      }
    }
    throw new AssertionError("No product with available stock on the current page");
  }
}
