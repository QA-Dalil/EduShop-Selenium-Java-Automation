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
}
