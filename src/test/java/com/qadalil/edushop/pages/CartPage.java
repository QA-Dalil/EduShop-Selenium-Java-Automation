package com.qadalil.edushop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CartPage extends BasePage {
  public CartPage(WebDriver driver) { super(driver); }

  public boolean hasItems() {
    visible(By.xpath("//h2[contains(.,'Shopping Cart')]"));
    wait.until(ExpectedConditions.or(
        ExpectedConditions.presenceOfElementLocated(By.xpath("//h5[normalize-space()='Order Summary']")),
        ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[normalize-space()='Your cart is empty']"))));
    return !driver.findElements(By.xpath("//h5[normalize-space()='Order Summary']")).isEmpty();
  }

  public CheckoutPage checkout() {
    click(By.cssSelector("a[href='/checkout']"));
    return new CheckoutPage(driver);
  }

  public int totalItems() {
    return Integer.parseInt(visible(By.xpath("//h5[normalize-space()='Order Summary']/following-sibling::div[1]/span[1]"))
        .getText().replaceAll("[^0-9]", ""));
  }

  public CartPage increaseFirstQuantity() {
    click(By.xpath("(//div[contains(@class,'border-bottom')][.//button[normalize-space()='+']]//button[normalize-space()='+'])[1]"));
    wait.until(driver -> totalItems() >= 2);
    return this;
  }

  public CartPage removeFirstItem() {
    click(By.cssSelector(".border-bottom button.btn-outline-danger"));
    wait.until(driver -> !hasItems());
    return this;
  }

  public CartPage clear() {
    click(By.xpath("//button[contains(normalize-space(.),'Clear Cart')]"));
    wait.until(driver -> !hasItems());
    return this;
  }
}
