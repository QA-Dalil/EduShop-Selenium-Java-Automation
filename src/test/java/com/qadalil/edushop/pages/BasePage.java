package com.qadalil.edushop.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
  protected final WebDriver driver;
  protected final WebDriverWait wait;

  protected BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
  }

  protected WebElement visible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected WebElement clickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  protected void type(By locator, String value) {
    WebElement input = visible(locator);
    input.clear();
    input.sendKeys(value);
  }

  protected void click(By locator) {
    clickable(locator).click();
  }

  protected void pathIs(String path) {
    String baseUrl = System.getProperty("baseUrl", "http://localhost:4200").replaceAll("/+$", "");
    if (path.isEmpty()) {
      wait.until(driver -> driver.getCurrentUrl().replaceAll("/+$", "").equals(baseUrl));
    } else {
      wait.until(ExpectedConditions.urlToBe(baseUrl + path));
    }
  }
}
